package com.teb.practice.consumer;

import static com.teb.practice.constants.TopicConstants.ORDER_CREATED;
import static com.teb.practice.constants.TopicConstants.PAYMENT_PROCESSED;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

import com.teb.practice.model.OrderEvent;
import com.teb.practice.model.PaymentEvent;
import com.teb.practice.util.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final JsonUtil jsonUtil;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = ORDER_CREATED, groupId = "order-service-group")
    public void consume(String message) {

        try {
            OrderEvent event = jsonUtil.fromJson(message, OrderEvent.class);

            log.info(
                    "Received OrderEvent: orderId={}, eventId={}, status={}",
                    event.getOrderId(),
                    event.getEventId(),
                    event.getStatus());

            PaymentEvent paymentEvent =
                    PaymentEvent.builder()
                            .eventId(randomUUID().toString())
                            .orderId(event.getOrderId())
                            .paymentId(randomUUID().toString())
                            .status("SUCCESS")
                            .createdAt(now())
                            .build();

            kafkaTemplate.send(
                    PAYMENT_PROCESSED, paymentEvent.getOrderId(), jsonUtil.toJson(paymentEvent));
        } catch (Exception e) {
            log.error("Error processing OrderEvent: {}", message, e);
        }
    }
}
