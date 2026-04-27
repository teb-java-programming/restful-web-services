package com.teb.practice.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.teb.practice.idempotency.IdempotencyService;
import com.teb.practice.model.EventMessage;
import com.teb.practice.retry.RetryHandler;
import com.teb.practice.service.MessageProcessingService;
import com.teb.practice.util.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitConsumerService {

    private final IdempotencyService idempotencyService;
    private final JsonUtil jsonUtil;
    private final MessageProcessingService messageProcessingService;
    private final RetryHandler retryHandler;

    @RabbitListener(queues = "messaging-basic-queue")
    public void consume(Message message, Channel channel) throws Exception {

        EventMessage eventMessage =
                jsonUtil.fromJson(new String(message.getBody()), EventMessage.class);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String messageId = eventMessage.getId();

        if (idempotencyService.isAlreadyProcessed(messageId)) {
            log.info("Duplicate message: {}", messageId);
            channel.basicAck(deliveryTag, false);

            return;
        }

        try {
            messageProcessingService.process(eventMessage, 1);
            idempotencyService.markAsProcessed(messageId);
            channel.basicAck(deliveryTag, false);
        } catch (Exception exception) {
            retryHandler.retryRabbitMessage(messageId, channel, deliveryTag);
        }
    }
}
