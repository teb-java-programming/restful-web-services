package com.teb.practice.service;

import com.teb.practice.model.EventMessage;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageProcessingService {

    public void process(EventMessage eventMessage, int flag) {

        String messageId = eventMessage.getId();

        if (flag == 0) {
            log.info("Processing Kafka message: {}", messageId);
        } else {
            log.info("Processing RabbitMQ message: {}", messageId);
        }

        if ("fail".equalsIgnoreCase(eventMessage.getMessage())) {
            throw new RuntimeException("Simulated event failed");
        }
    }
}
