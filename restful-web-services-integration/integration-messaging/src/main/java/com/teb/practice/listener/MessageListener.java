package com.teb.practice.listener;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageListener {

    private static final int MAX_RETRIES = 3;

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = "DEV.QUEUE.1")
    public void receive(Message jmsMessage) throws JMSException {

        String payload =
                jmsMessage.getBody(String.class);

        int retryCount =
                jmsMessage.propertyExists("retryCount")
                        ? jmsMessage.getIntProperty("retryCount")
                        : 0;


        try {

            if ("fail".equalsIgnoreCase(payload)) {
                throw new RuntimeException("Simulated processing failure");
            }

            System.out.println("Received from MQ: " + payload);

        } catch (Exception ex) {

            if (retryCount < MAX_RETRIES) {

                int nextRetry = retryCount + 1;

                System.err.println(
                        "Retrying message: "
                                + payload
                                + " | attempt="
                                + nextRetry);

                jmsTemplate.convertAndSend(
                        "DEV.QUEUE.1",
                        payload,
                        message -> {
                            message.setIntProperty(
                                    "retryCount",
                                    nextRetry);
                            return message;
                        });

            } else {

                System.err.println(
                        "Moving message to DLQ: "
                                + payload);

                jmsTemplate.convertAndSend(
                        "DEV.DEAD.LETTER.QUEUE",
                        payload);
            }
        }
    }
}
