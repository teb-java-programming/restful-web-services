package com.teb.practice.idempotency;

import static java.util.concurrent.ConcurrentHashMap.newKeySet;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class IdempotencyService {

    private final Set<String> processedMessageIds = newKeySet();

    public boolean isAlreadyProcessed(String messageId) {

        log.info("Idempotency check for: {}", messageId);

        return processedMessageIds.contains(messageId);
    }

    public void markAsProcessed(String messageId) {

        log.info("Processed message: {}", messageId);

        processedMessageIds.add(messageId);
    }
}
