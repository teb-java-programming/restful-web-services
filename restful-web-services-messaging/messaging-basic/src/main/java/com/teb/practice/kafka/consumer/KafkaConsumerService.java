package com.teb.practice.kafka.consumer;

import static com.teb.practice.constants.MessagingConstants.MESSAGING_BASIC_GROUP;
import static com.teb.practice.constants.MessagingConstants.MESSAGING_BASIC_TOPIC;

import com.teb.practice.idempotency.IdempotencyService;
import com.teb.practice.model.EventMessage;
import com.teb.practice.retry.RetryHandler;
import com.teb.practice.service.MessageProcessingService;
import com.teb.practice.util.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final IdempotencyService idempotencyService;
    private final JsonUtil jsonUtil;
    private final MessageProcessingService messageProcessingService;
    private final RetryHandler retryHandler;

    @KafkaListener(topics = MESSAGING_BASIC_TOPIC, groupId = MESSAGING_BASIC_GROUP)
    public void consume(String message, Acknowledgment acknowledgment) {

        EventMessage eventMessage = jsonUtil.fromJson(message, EventMessage.class);
        String messageId = eventMessage.getId();

        if (idempotencyService.isAlreadyProcessed(messageId)) {
            log.info("Duplicate message: {}", messageId);
            acknowledgment.acknowledge();

            return;
        }

        try {
            messageProcessingService.process(eventMessage, 0);
            idempotencyService.markAsProcessed(messageId);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            retryHandler.retryKafkaMessage(messageId, acknowledgment);
        }
    }
}
