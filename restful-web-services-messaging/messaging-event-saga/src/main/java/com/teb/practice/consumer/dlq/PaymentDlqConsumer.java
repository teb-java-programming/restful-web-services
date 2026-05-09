package com.teb.practice.consumer.dlq;

import static com.teb.practice.event.KafkaTopics.PAYMENT_DLQ;

import com.teb.practice.event.SagaEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentDlqConsumer {

    @KafkaListener(topics = PAYMENT_DLQ, groupId = "saga-dlq-group")
    public void consume(SagaEvent event) {

        log.error(
                "[{}] Payment DLQ event={}",
                event != null ? event.getSagaId() : "UNKNOWN",
                event);
    }
}
