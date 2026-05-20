package com.teb.practice.config;

import com.ibm.mq.jakarta.jms.MQConnectionFactory;
import com.ibm.msg.client.jakarta.wmq.WMQConstants;
import jakarta.jms.ConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelJmsConfig {

    @Bean
    public ConnectionFactory mqConnectionFactory() throws Exception {

        MQConnectionFactory factory = new MQConnectionFactory();

        factory.setHostName("localhost");
        factory.setPort(1414);
        factory.setQueueManager("QM1");
        factory.setChannel("DEV.APP.SVRCONN");
        factory.setTransportType(WMQConstants.WMQ_CM_CLIENT);

        factory.setStringProperty(WMQConstants.USERID, "app");
        factory.setStringProperty(WMQConstants.PASSWORD, "app123!");

        return factory;
    }

    @Bean
    public JmsComponent jms(ConnectionFactory mqConnectionFactory) {

        JmsComponent jms = new JmsComponent();
        jms.setConnectionFactory(mqConnectionFactory);

        return jms;
    }
}