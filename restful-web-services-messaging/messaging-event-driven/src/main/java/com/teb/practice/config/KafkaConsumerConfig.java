package com.teb.practice.config;

import static org.springframework.kafka.config.TopicBuilder.name;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public NewTopic orderCreatedTopic() {

        return name("order.created").partitions(4).replicas(1).build();
    }

    @Bean
    public NewTopic paymentProcessedTopic() {

        return name("payment.processed").partitions(4).replicas(1).build();
    }

    @Bean
    public NewTopic inventoryReservedTopic() {

        return name("inventory.reserved").partitions(4).replicas(1).build();
    }

    @Bean
    public NewTopic notificationSentTopic() {

        return name("notification.sent").partitions(4).replicas(1).build();
    }
}
