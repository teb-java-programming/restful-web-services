package com.teb.practice.consumer;

import static com.teb.practice.constants.TopicConstants.NOTIFICATION_SENT;

import com.teb.practice.model.NotificationEvent;
import com.teb.practice.util.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final JsonUtil jsonUtil;

    @KafkaListener(topics = NOTIFICATION_SENT, groupId = "notification-service-group")
    public void consume(String message) {

        try {
            NotificationEvent event = jsonUtil.fromJson(message, NotificationEvent.class);

            log.info(
                    "Notification sent: orderId={}, message={}",
                    event.getOrderId(),
                    event.getMessage());
        } catch (Exception e) {
            log.error("Error processing NotificationEvent: {}", message, e);
        }
    }
}
