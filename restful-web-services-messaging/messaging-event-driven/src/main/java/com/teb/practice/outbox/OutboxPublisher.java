package com.teb.practice.outbox;

import static com.teb.practice.constants.TopicConstants.ORDER_CREATED;

import com.teb.practice.entity.OutboxEventEntity;
import com.teb.practice.repository.OutboxRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    public void publish() {

        List<OutboxEventEntity> events = outboxRepository.findByStatus("PENDING");

        for (OutboxEventEntity event : events) {
            kafkaTemplate.send(ORDER_CREATED, event.getAggregateId(), event.getPayload());
            event.setStatus("SENT");

            outboxRepository.save(event);
        }
    }
}
