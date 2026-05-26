package com.teb.practice.controller;

import static java.lang.System.currentTimeMillis;
import static java.util.UUID.randomUUID;

import com.teb.practice.model.MessageRequest;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private static final String QUEUE_NAME = "TEB.MSG.QUEUE";
    private static final String MESSAGE_STATUS = "status";

    private final JmsTemplate jmsTemplate;

    @PostMapping("/send")
    public Map<String, String> send(@Valid @RequestBody MessageRequest request) {

        String requestMessage = request.message();

        jmsTemplate.convertAndSend(
                QUEUE_NAME,
                requestMessage,
                message -> {
                    message.setStringProperty("traceId", randomUUID().toString());
                    message.setLongProperty("startTime", currentTimeMillis());

                    return message;
                });

        return Map.of(MESSAGE_STATUS, "SENT", "queue", QUEUE_NAME, "message", requestMessage);
    }

    @GetMapping("/health")
    public Map<String, String> health() {

        return Map.of(MESSAGE_STATUS, "UP");
    }
}
