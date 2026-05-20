package com.teb.practice.controller;

import lombok.RequiredArgsConstructor;

import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/route")
@RequiredArgsConstructor
public class RoutingController {

    private final ProducerTemplate producerTemplate;

    @PostMapping("/send")
    public Map<String, String> send(@RequestBody Map<String, String> request) {

        producerTemplate.sendBody("direct:process-message", request.get("message"));

        return Map.of("status", "ROUTED", "message", request.get("message"));
    }
}
