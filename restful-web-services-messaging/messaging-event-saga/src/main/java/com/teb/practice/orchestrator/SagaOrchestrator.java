package com.teb.practice.orchestrator;

import static com.teb.practice.event.KafkaTopics.INVENTORY_RESERVED;
import static com.teb.practice.event.KafkaTopics.NOTIFICATION_SENT;
import static com.teb.practice.event.KafkaTopics.PAYMENT_COMPLETED;

import com.teb.practice.event.SagaEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SagaOrchestrator {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishInventoryStep(SagaEvent sagaEvent) {

        log.info("[{}] publishing [INVENTORY] event", sagaEvent.getSagaId());
        kafkaTemplate.send(INVENTORY_RESERVED, sagaEvent.getSagaId(), sagaEvent);
    }

    public void publishPaymentStep(SagaEvent sagaEvent) {

        log.info("[{}] publishing [PAYMENT] event", sagaEvent.getSagaId());
        kafkaTemplate.send(PAYMENT_COMPLETED, sagaEvent.getSagaId(), sagaEvent);
    }

    public void publishNotificationStep(SagaEvent sagaEvent) {

        log.info("[{}] publishing [NOTIFICATION] event", sagaEvent.getSagaId());
        kafkaTemplate.send(NOTIFICATION_SENT, sagaEvent.getSagaId(), sagaEvent);
    }
}
