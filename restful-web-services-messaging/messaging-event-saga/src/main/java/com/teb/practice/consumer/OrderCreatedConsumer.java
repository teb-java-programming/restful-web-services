package com.teb.practice.consumer;

import static com.teb.practice.event.KafkaTopics.ORDER_CREATED;
import static com.teb.practice.event.SagaStatus.INVENTORY_PENDING;
import static java.time.LocalDateTime.now;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teb.practice.entity.EventStatus;
import com.teb.practice.event.SagaEvent;
import com.teb.practice.repository.EventStatusRepository;
import com.teb.practice.service.SagaOrchestrator;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

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

        Assert.notNull(event, "ORDER event must not be null");

        String sagaId = event.getSagaId();

        Optional<EventStatus> existing =
                eventStatusRepository.findBySagaIdAndEventType(sagaId, "ORDER");

        if (existing.isPresent()) {
            log.info("[SAGA:{}] ORDER already processed", sagaId);
            return;
        }

        log.info("[SAGA:{}] [ORDER] received event: {}", sagaId, event);

        if (event.isForceFail()) {
            throw new RuntimeException("DLQ triggered");
        }

        eventStatusRepository.save(
                EventStatus.builder()
                        .sagaId(sagaId)
                        .eventType("ORDER")
                        .status("RECEIVED")
                        .payload(objectMapper.convertValue(event, new TypeReference<>() {}))
                        .retryCount(0)
                        .createdAt(now())
                        .updatedAt(now())
                        .build());

        event.setStatus(INVENTORY_PENDING);

        sagaOrchestrator.publishInventoryStep(event);
    }
}
