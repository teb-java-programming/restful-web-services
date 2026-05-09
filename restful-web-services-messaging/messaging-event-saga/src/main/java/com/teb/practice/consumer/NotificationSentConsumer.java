package com.teb.practice.consumer;

import static com.teb.practice.event.EventTypes.NOTIFICATION;
import static com.teb.practice.event.KafkaTopics.NOTIFICATION_SENT;
import static com.teb.practice.event.SagaStatus.SAGA_COMPLETED;

import static org.springframework.util.Assert.notNull;

import static java.time.LocalDateTime.now;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teb.practice.entity.EventStatus;
import com.teb.practice.event.SagaEvent;
import com.teb.practice.repository.EventStatusRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationSentConsumer {

    private final EventStatusRepository eventStatusRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @KafkaListener(topics = NOTIFICATION_SENT, groupId = "saga-group")
    public void consume(SagaEvent sagaEvent) {

        notNull(sagaEvent, "Notification event must not be null");

        String sagaId = sagaEvent.getSagaId();

        if (eventStatusRepository
                .findBySagaIdAndEventType(sagaId, NOTIFICATION.name())
                .isPresent()) {
            log.info("[{}] Notification already processed", sagaId);

            return;
        }

        eventStatusRepository.save(
                EventStatus.builder()
                        .sagaId(sagaId)
                        .eventType(NOTIFICATION.name())
                        .status("SENT")
                        .payload(objectMapper.convertValue(sagaEvent, new TypeReference<>() {}))
                        .createdAt(now())
                        .updatedAt(now())
                        .build());

        log.info("[{}] Notification sent event: {}", sagaId, sagaEvent);

        sagaEvent.setStatus(SAGA_COMPLETED);
    }
}
