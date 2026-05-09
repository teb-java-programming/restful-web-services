package com.teb.practice.consumer;

import static com.teb.practice.event.EventTypes.INVENTORY;
import static com.teb.practice.event.EventTypes.ORDER;
import static com.teb.practice.event.KafkaTopics.ORDER_CREATED;
import static com.teb.practice.event.SagaStatus.INVENTORY_PENDING;

import static org.springframework.util.Assert.notNull;

import static java.time.LocalDateTime.now;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teb.practice.entity.EventStatus;
import com.teb.practice.event.SagaEvent;
import com.teb.practice.orchestrator.SagaOrchestrator;
import com.teb.practice.repository.EventStatusRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedConsumer {

    private final EventStatusRepository eventStatusRepository;
    private final ObjectMapper objectMapper;
    private final SagaOrchestrator sagaOrchestrator;

    @Transactional
    @KafkaListener(topics = ORDER_CREATED, groupId = "saga-group")
    public void consume(SagaEvent event) {

        notNull(event, "Order event must not be null");

        String sagaId = event.getSagaId();

        if (eventStatusRepository.findBySagaIdAndEventType(sagaId, ORDER.name()).isPresent()) {
            log.info("[{}] Order already processed", sagaId);

            return;
        }

        if (ORDER.name().equals(event.getFailAt())) {
            throw new RuntimeException("DLQ triggered");
        }

        eventStatusRepository.save(
                EventStatus.builder()
                        .sagaId(sagaId)
                        .eventType(ORDER.name())
                        .status("RECEIVED")
                        .payload(objectMapper.convertValue(event, new TypeReference<>() {}))
                        .createdAt(now())
                        .updatedAt(now())
                        .build());

        event.setCurrentStage(INVENTORY.name());
        event.setStatus(INVENTORY_PENDING);

        log.info("[{}] Order received event: {}", sagaId, event);

        sagaOrchestrator.publishInventoryStep(event);
    }
}
