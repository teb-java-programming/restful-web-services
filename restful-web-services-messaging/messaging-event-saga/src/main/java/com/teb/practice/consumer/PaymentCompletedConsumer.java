package com.teb.practice.consumer;

import static com.teb.practice.event.SagaStatus.PAYMENT_COMPLETED;
import static java.time.LocalDateTime.now;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teb.practice.entity.EventStatus;
import com.teb.practice.event.KafkaTopics;
import com.teb.practice.event.SagaEvent;
import com.teb.practice.repository.EventStatusRepository;
import com.teb.practice.service.SagaOrchestrator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCompletedConsumer {

    
    private final EventStatusRepository eventStatusRepository;
    private final ObjectMapper objectMapper;
    private final SagaOrchestrator sagaOrchestrator;

    @Transactional
    @KafkaListener(topics = KafkaTopics.PAYMENT_COMPLETED, groupId = "saga-group")
    public void consume(SagaEvent event) {

        Assert.notNull(event, "PAYMENT event must not be null");

        String sagaId = event.getSagaId();

        Optional<EventStatus> existing =
                eventStatusRepository.findBySagaIdAndEventType(sagaId, "PAYMENT");

        if (existing.isPresent()) {
            log.info("[SAGA:{}] PAYMENT already processed", sagaId);
            return;
        }

        log.info("[SAGA:{}] [PAYMENT] completed event: {}", sagaId, event);

        eventStatusRepository.save(
                EventStatus.builder()
                        .sagaId(sagaId)
                        .eventType("PAYMENT")
                        .status("COMPLETED")
                        .payload(objectMapper.convertValue(event, new TypeReference<>() {}))
                        .retryCount(0)
                        .createdAt(now())
                        .updatedAt(now())
                        .build());

        event.setStatus(PAYMENT_COMPLETED);

        sagaOrchestrator.publishNotificationStep(event);
    }
}
