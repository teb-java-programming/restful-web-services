package com.teb.practice.config;

import static com.teb.practice.event.KafkaTopics.INVENTORY_DLQ;
import static com.teb.practice.event.KafkaTopics.INVENTORY_RESERVED;
import static com.teb.practice.event.KafkaTopics.NOTIFICATION_SENT;
import static com.teb.practice.event.KafkaTopics.ORDER_CREATED;
import static com.teb.practice.event.KafkaTopics.ORDER_DLQ;
import static com.teb.practice.event.KafkaTopics.PAYMENT_COMPLETED;
import static com.teb.practice.event.KafkaTopics.PAYMENT_DLQ;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderTopic() {

        return new NewTopic(ORDER_CREATED, 4, (short) 1);
    }

    @Bean
    public NewTopic inventoryTopic() {

        return new NewTopic(INVENTORY_RESERVED, 4, (short) 1);
    }

    @Bean
    public NewTopic paymentTopic() {

        return new NewTopic(PAYMENT_COMPLETED, 4, (short) 1);
    }

    @Bean
    public NewTopic notificationTopic() {

        return new NewTopic(NOTIFICATION_SENT, 4, (short) 1);
    }

    @Bean
    public NewTopic orderDlq() {

        return new NewTopic(ORDER_DLQ, 4, (short) 1);
    }

    @Bean
    public NewTopic inventoryDlq() {

        return new NewTopic(INVENTORY_DLQ, 4, (short) 1);
    }

    @Bean
    public NewTopic paymentDlq() {

        return new NewTopic(PAYMENT_DLQ, 4, (short) 1);
    }
}
