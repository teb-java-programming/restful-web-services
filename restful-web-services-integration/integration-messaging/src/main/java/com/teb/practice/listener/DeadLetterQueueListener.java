package com.teb.practice.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterQueueListener {

    @JmsListener(destination = "DEV.DEAD.LETTER.QUEUE")
    public void receive(String message) {

        System.err.println(
                "Received from DLQ: " + message);
    }
}