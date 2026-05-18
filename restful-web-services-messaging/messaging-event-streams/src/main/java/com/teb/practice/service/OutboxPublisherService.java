package com.teb.practice.service;

import com.teb.practice.model.RideOutboxEntity;
import com.teb.practice.producer.RideEventProducer;
import com.teb.practice.repository.RideOutboxRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxPublisherService {

    private final RideOutboxRepository outboxRepository;
    private final RideEventProducer eventProducer;

    @Scheduled(fixedDelay = 1000)
    public void publish() {

        for (RideOutboxEntity outbox : outboxRepository.findByProcessedFalse()) {
            eventProducer.publishRaw(outbox.getAggregateId(), outbox.getPayloadJson());
            outbox.setProcessed(true);
            outboxRepository.save(outbox);
        }
    }
}
