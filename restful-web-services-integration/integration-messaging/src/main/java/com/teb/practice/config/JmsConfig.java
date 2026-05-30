package com.teb.practice.config;

import static com.ibm.msg.client.jakarta.wmq.WMQConstants.PASSWORD;
import static com.ibm.msg.client.jakarta.wmq.WMQConstants.USERID;
import static com.ibm.msg.client.jakarta.wmq.WMQConstants.WMQ_CM_CLIENT;

import com.ibm.mq.jakarta.jms.MQConnectionFactory;

import jakarta.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class JmsConfig {

    @Value("${ibm.mq.host}")
    private String host;

    @Value("${ibm.mq.port}")
    private int port;

    @Value("${ibm.mq.queue-manager}")
    private String queueManager;

    @Value("${ibm.mq.channel}")
    private String channel;

    @Value("${ibm.mq.username}")
    private String username;

    @Value("${ibm.mq.password}")
    private String password;

    @Bean
    public ConnectionFactory connectionFactory() throws Exception {

        MQConnectionFactory factory = new MQConnectionFactory();

        factory.setHostName(host);
        factory.setPort(port);
        factory.setQueueManager(queueManager);
        factory.setChannel(channel);
        factory.setTransportType(WMQ_CM_CLIENT);
        factory.setStringProperty(USERID, username);
        factory.setStringProperty(PASSWORD, password);

        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {

        return new JmsTemplate(connectionFactory);
    }
}
