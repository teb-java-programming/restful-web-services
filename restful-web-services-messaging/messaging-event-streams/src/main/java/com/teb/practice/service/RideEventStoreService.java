package com.teb.practice.service;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

import com.teb.practice.event.RideEvent;
import com.teb.practice.model.RideEventEntity;
import com.teb.practice.model.RideOutboxEntity;
import com.teb.practice.repository.RideEventRepository;
import com.teb.practice.repository.RideOutboxRepository;
import com.teb.practice.util.JsonUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideEventStoreService {

    private final RideEventRepository eventRepository;
    private final RideOutboxRepository outboxRepository;
    private final JsonUtil jsonUtil;

    public void store(RideEvent event) {

        String json = jsonUtil.toJson(event);

        // Event store history
        eventRepository.save(
                RideEventEntity.builder()
                        .eventId(randomUUID().toString())
                        .rideId(event.getRideId())
                        .eventType(event.getEventType().name())
                        .payloadJson(json)
                        .createdAt(now())
                        .build());

        // Outbox publish buffer
        outboxRepository.save(
                RideOutboxEntity.builder()
                        .eventId(randomUUID().toString())
                        .aggregateId(event.getRideId())
                        .eventType(event.getEventType().name())
                        .payloadJson(json)
                        .processed(false)
                        .createdAt(now())
                        .build());
    }
}
