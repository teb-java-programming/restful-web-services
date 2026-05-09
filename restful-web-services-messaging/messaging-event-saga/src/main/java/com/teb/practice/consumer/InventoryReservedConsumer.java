package com.teb.practice.consumer;

import static com.teb.practice.event.EventTypes.INVENTORY;
import static com.teb.practice.event.KafkaTopics.INVENTORY_RESERVED;
import static com.teb.practice.event.SagaStatus.PAYMENT_PENDING;

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
public class InventoryReservedConsumer {

    private final EventStatusRepository eventStatusRepository;
    private final ObjectMapper objectMapper;
    private final SagaOrchestrator sagaOrchestrator;

    @Transactional
    @KafkaListener(topics = INVENTORY_RESERVED, groupId = "saga-group")
    public void consume(SagaEvent event) {

        notNull(event, "Inventory event must not be null");

        String sagaId = event.getSagaId();

        if (eventStatusRepository.findBySagaIdAndEventType(sagaId, INVENTORY.name()).isPresent()) {
            log.info("[{}] Inventory already processed", sagaId);

            return;
        }

        eventStatusRepository.save(
                EventStatus.builder()
                        .sagaId(sagaId)
                        .eventType(INVENTORY.name())
                        .status("RESERVED")
                        .payload(objectMapper.convertValue(event, new TypeReference<>() {}))
                        .createdAt(now())
                        .updatedAt(now())
                        .build());

        event.setCurrentStage("PAYMENT");
        event.setStatus(PAYMENT_PENDING);

        log.info("[{}] Inventory reserved event: {}", sagaId, event);

        sagaOrchestrator.publishPaymentStep(event);
    }
}
