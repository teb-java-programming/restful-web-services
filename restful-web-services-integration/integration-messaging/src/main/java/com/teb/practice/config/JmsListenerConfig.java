package com.teb.practice.config;

import jakarta.jms.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.util.ErrorHandler;

@Configuration
@EnableJms
public class JmsListenerConfig {

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            ConnectionFactory connectionFactory, ErrorHandler errorHandler) {

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("1-3");
        factory.setSessionTransacted(false);
        factory.setErrorHandler(errorHandler);

        return factory;
    }

    @Bean
    public ErrorHandler errorHandler() {

        return throwable -> System.err.println("JMS listener error: " + throwable.getMessage());
    }
}
