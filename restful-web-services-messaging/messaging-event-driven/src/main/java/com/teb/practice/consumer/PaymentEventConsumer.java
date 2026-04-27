package com.teb.practice.consumer;

import static com.teb.practice.constants.TopicConstants.INVENTORY_RESERVED;
import static com.teb.practice.constants.TopicConstants.PAYMENT_PROCESSED;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

import com.teb.practice.model.InventoryEvent;
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
public class PaymentEventConsumer {

    private final JsonUtil jsonUtil;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = PAYMENT_PROCESSED, groupId = "payment-service-group")
    public void consume(String message) {

        try {
            PaymentEvent event = jsonUtil.fromJson(message, PaymentEvent.class);

            log.info(
                    "Received PaymentEvent: orderId={}, paymentId={}, status={}",
                    event.getOrderId(),
                    event.getPaymentId(),
                    event.getStatus());

            InventoryEvent inventoryEvent =
                    InventoryEvent.builder()
                            .eventId(randomUUID().toString())
                            .orderId(event.getOrderId())
                            .inventoryId(randomUUID().toString())
                            .status("RESERVED")
                            .createdAt(now())
                            .build();

            kafkaTemplate.send(
                    INVENTORY_RESERVED,
                    inventoryEvent.getOrderId(),
                    jsonUtil.toJson(inventoryEvent));
        } catch (Exception e) {
            log.error("Error processing PaymentEvent: {}", message, e);
        }
    }
}
