package com.teb.practice.controller;

import com.teb.practice.model.MessageRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final JmsTemplate jmsTemplate;

    @PostMapping("/send")
    public Map<String, String> send(@Valid @RequestBody MessageRequest request) {

        jmsTemplate.convertAndSend("DEV.QUEUE.1", request.message());

        return Map.of(
                "status", "SENT",
                "queue", "DEV.QUEUE.1");
    }

    @GetMapping("/health")
    public Map<String, String> health() {

        return Map.of("status", "UP");
    }
}
