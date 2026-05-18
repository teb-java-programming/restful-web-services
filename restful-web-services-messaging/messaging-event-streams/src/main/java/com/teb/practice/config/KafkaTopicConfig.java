package com.teb.practice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    public static final String RIDE_EVENTS_LIFECYCLE_TOPIC = "ride.events.lifecycle";
    public static final String RIDE_EVENTS_PROCESSED_TOPIC = "ride.events.processed";

    @Bean
    public NewTopic rideLifecycleTopic() {

        return new NewTopic(RIDE_EVENTS_LIFECYCLE_TOPIC, 1, (short) 1);
    }

    @Bean
    public NewTopic rideProcessedTopic() {

        return new NewTopic(RIDE_EVENTS_PROCESSED_TOPIC, 1, (short) 1);
    }
}
