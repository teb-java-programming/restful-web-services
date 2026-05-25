package com.teb.practice.config;

import static com.ibm.msg.client.jakarta.wmq.WMQConstants.PASSWORD;
import static com.ibm.msg.client.jakarta.wmq.WMQConstants.USERID;
import static com.ibm.msg.client.jakarta.wmq.WMQConstants.WMQ_CM_CLIENT;

import com.ibm.mq.jakarta.jms.MQConnectionFactory;

import jakarta.jms.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class JmsConfig {

    @Bean
    public ConnectionFactory connectionFactory() throws Exception {

        MQConnectionFactory factory = new MQConnectionFactory();

        factory.setHostName("localhost");
        factory.setPort(1414);
        factory.setQueueManager("QM1");
        factory.setChannel("DEV.APP.SVRCONN");
        factory.setTransportType(WMQ_CM_CLIENT);
        factory.setStringProperty(USERID, "app");
        factory.setStringProperty(PASSWORD, "app123!");

        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {

        return new JmsTemplate(connectionFactory);
    }
}
