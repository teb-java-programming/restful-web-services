package com.teb.practice.consumer;

import static com.teb.practice.event.SagaStatus.NOTIFICATION_SENT;
import static java.time.LocalDateTime.now;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teb.practice.entity.EventStatus;
import com.teb.practice.event.KafkaTopics;
import com.teb.practice.event.SagaEvent;
import com.teb.practice.repository.EventStatusRepository;

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
public class NotificationSentConsumer {

    private final EventStatusRepository eventStatusRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @KafkaListener(topics = KafkaTopics.NOTIFICATION_SENT, groupId = "saga-group")
    public void consume(SagaEvent event) {

        Assert.notNull(event, "NOTIFICATION event must not be null");

        String sagaId = event.getSagaId();

        Optional<EventStatus> existing =
                eventStatusRepository.findBySagaIdAndEventType(sagaId, "NOTIFICATION");

        if (existing.isPresent()) {
            log.info("[SAGA:{}] NOTIFICATION already processed", sagaId);
            return;
        }

        log.info("[SAGA:{}] [NOTIFICATION] sent event: {}", sagaId, event);

        eventStatusRepository.save(
                EventStatus.builder()
                        .sagaId(sagaId)
                        .eventType("NOTIFICATION")
                        .status("SENT")
                        .payload(objectMapper.convertValue(event, new TypeReference<>() {}))
                        .retryCount(0)
                        .createdAt(now())
                        .updatedAt(now())
                        .build());

        event.setStatus(NOTIFICATION_SENT);
    }
}
