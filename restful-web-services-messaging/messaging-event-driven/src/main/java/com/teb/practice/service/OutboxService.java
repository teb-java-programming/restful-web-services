package com.teb.practice.service;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

import com.teb.practice.entity.OutboxEventEntity;
import com.teb.practice.repository.OutboxRepository;
import com.teb.practice.util.JsonUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final JsonUtil jsonUtil;
    private final OutboxRepository repository;

    public void save(String aggregateId, String eventType, Object payload) {

        try {
            repository.save(
                    OutboxEventEntity.builder()
                            .id(randomUUID())
                            .aggregateType("ORDER")
                            .aggregateId(aggregateId)
                            .eventType(eventType)
                            .payload(jsonUtil.toJson(payload))
                            .status("PENDING")
                            .createdAt(now())
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
