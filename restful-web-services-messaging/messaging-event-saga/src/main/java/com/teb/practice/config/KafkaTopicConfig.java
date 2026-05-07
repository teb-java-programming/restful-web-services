package com.teb.practice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderTopic() {

        return new NewTopic("order.created", 4, (short) 1);
    }

    @Bean
    public NewTopic inventoryTopic() {

        return new NewTopic("inventory.reserved", 4, (short) 1);
    }

    @Bean
    public NewTopic paymentTopic() {

        return new NewTopic("payment.completed", 4, (short) 1);
    }

    @Bean
    public NewTopic notificationTopic() {

        return new NewTopic("notification.sent", 4, (short) 1);
    }

    @Bean
    public NewTopic orderDlq() {

        return new NewTopic("order.created.dlq", 4, (short) 1);
    }

    @Bean
    public NewTopic inventoryDlq() {

        return new NewTopic("inventory.reserved.dlq", 4, (short) 1);
    }

    @Bean
    public NewTopic paymentDlq() {

        return new NewTopic("payment.completed.dlq", 4, (short) 1);
    }
}