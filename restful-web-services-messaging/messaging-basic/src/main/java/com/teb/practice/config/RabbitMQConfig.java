package com.teb.practice.config;

import static com.teb.practice.constants.MessagingConstants.MESSAGING_BASIC_EXCHANGE;
import static com.teb.practice.constants.MessagingConstants.MESSAGING_BASIC_QUEUE;
import static com.teb.practice.constants.MessagingConstants.ROUTING_KEY;

import static org.springframework.amqp.core.BindingBuilder.bind;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange exchange() {

        return new TopicExchange(MESSAGING_BASIC_EXCHANGE);
    }

    @Bean
    public Queue queue() {

        return new Queue(MESSAGING_BASIC_QUEUE, true);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {

        return bind(queue).to(exchange).with(ROUTING_KEY);
    }
}
