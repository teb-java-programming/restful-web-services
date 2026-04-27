package com.teb.practice.consumer;

import static com.teb.practice.constants.TopicConstants.INVENTORY_RESERVED;
import static com.teb.practice.constants.TopicConstants.NOTIFICATION_SENT;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

import com.teb.practice.model.InventoryEvent;
import com.teb.practice.model.NotificationEvent;
import com.teb.practice.util.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InventoryEventConsumer {

    private final JsonUtil jsonUtil;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = INVENTORY_RESERVED, groupId = "inventory-service-group")
    public void consume(String message) {

        try {
            InventoryEvent event = jsonUtil.fromJson(message, InventoryEvent.class);

            log.info(
                    "Received InventoryEvent: orderId={}, inventoryId={}, status={}",
                    event.getOrderId(),
                    event.getInventoryId(),
                    event.getStatus());

            NotificationEvent notificationEvent =
                    NotificationEvent.builder()
                            .eventId(randomUUID().toString())
                            .orderId(event.getOrderId())
                            .message("Order processed")
                            .createdAt(now())
                            .build();

            kafkaTemplate.send(
                    NOTIFICATION_SENT,
                    notificationEvent.getOrderId(),
                    jsonUtil.toJson(notificationEvent));
        } catch (Exception e) {
            log.error("Error processing InventoryEvent: {}", message, e);
        }
    }
}
