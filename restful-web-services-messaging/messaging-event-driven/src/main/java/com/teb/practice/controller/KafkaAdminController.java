package com.teb.practice.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
@RequiredArgsConstructor
public class KafkaAdminController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/{topic}")
    public String send(
            @PathVariable String topic, @RequestParam String key, @RequestBody String message) {

        kafkaTemplate.send(topic, key, message);

        return "Message sent to topic: " + topic;
    }
}
