package com.teb.practice.consumer;

import static com.teb.practice.event.EventTypes.PAYMENT;
import static com.teb.practice.event.KafkaTopics.PAYMENT_COMPLETED;
import static com.teb.practice.event.SagaStatus.NOTIFICATION_PENDING;

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
public class PaymentCompletedConsumer {

    private final EventStatusRepository eventStatusRepository;
    private final ObjectMapper objectMapper;
    private final SagaOrchestrator sagaOrchestrator;

    @Transactional
    @KafkaListener(topics = PAYMENT_COMPLETED, groupId = "saga-group")
    public void consume(SagaEvent event) {

        notNull(event, "Payment event must not be null");

        String sagaId = event.getSagaId();

        if (eventStatusRepository.findBySagaIdAndEventType(sagaId, PAYMENT.name()).isPresent()) {
            log.info("[{}] Payment already processed", sagaId);

            return;
        }

        eventStatusRepository.save(
                EventStatus.builder()
                        .sagaId(sagaId)
                        .eventType(PAYMENT.name())
                        .status("COMPLETED")
                        .payload(objectMapper.convertValue(event, new TypeReference<>() {}))
                        .createdAt(now())
                        .updatedAt(now())
                        .build());

        event.setCurrentStage("NOTIFICATION");
        event.setStatus(NOTIFICATION_PENDING);

        log.info("[{}] Payment completed event: {}", sagaId, event);

        sagaOrchestrator.publishNotificationStep(event);
    }
}
