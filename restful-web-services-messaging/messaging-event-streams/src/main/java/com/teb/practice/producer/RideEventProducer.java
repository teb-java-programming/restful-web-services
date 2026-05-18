package com.teb.practice.producer;

import static com.teb.practice.config.KafkaTopicConfig.RIDE_EVENTS_LIFECYCLE_TOPIC;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RideEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publishRaw(String key, String payloadJson) {

        kafkaTemplate.send(RIDE_EVENTS_LIFECYCLE_TOPIC, key, payloadJson);
    }
}
