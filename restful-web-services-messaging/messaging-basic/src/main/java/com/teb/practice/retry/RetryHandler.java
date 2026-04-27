package com.teb.practice.retry;

import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Slf4j
public class RetryHandler {

    private static final ConcurrentMap<String, Integer> retryMap = new ConcurrentHashMap<>();

    public void retryKafkaMessage(String messageId, Acknowledgment acknowledgment) {

        int retryCount = retryMap.getOrDefault(messageId, 0) + 1;
        retryMap.put(messageId, retryCount);

        log.warn("Kafka retry attempt {} for {}", retryCount, messageId);

        if (retryCount < 3) {
            acknowledgment.nack(Duration.ofMillis(10));
        } else {
            log.error("Sending to Kafka DLQ: {}", messageId);

            retryMap.remove(messageId);
            acknowledgment.acknowledge();
        }
    }

    public void retryRabbitMessage(String messageId, Channel channel, long deliveryTag)
            throws IOException {

        int retryCount = retryMap.getOrDefault(messageId, 0) + 1;
        retryMap.put(messageId, retryCount);

        log.warn("RabbitMQ retry attempt {} for {}", retryCount, messageId);

        if (retryCount < 3) {
            channel.basicNack(deliveryTag, false, true);
        } else {
            log.error("Sending to Rabbit DLQ: {}", messageId);

            retryMap.remove(messageId);
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
