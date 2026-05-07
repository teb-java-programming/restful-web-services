package com.teb.practice.service;

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

    public void publishInventoryStep(SagaEvent event) {

        log.info("[SAGA:{}] publishing INVENTORY event", event.getSagaId());
        kafkaTemplate.send(INVENTORY_RESERVED, event.getSagaId(), event);
    }

    public void publishPaymentStep(SagaEvent event) {

        log.info("[SAGA:{}] publishing PAYMENT event", event.getSagaId());
        kafkaTemplate.send(PAYMENT_COMPLETED, event.getSagaId(), event);
    }

    public void publishNotificationStep(SagaEvent event) {

        log.info("[SAGA:{}] publishing NOTIFICATION event", event.getSagaId());
        kafkaTemplate.send(NOTIFICATION_SENT, event.getSagaId(), event);
    }
}