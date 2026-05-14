//package com.teb.practice.controller;
//
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.Instant;
//import java.util.Map;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/events")
//public class BankingEventController {
//
//    private final KafkaTemplate<String, Object> kafkaTemplate;
//
//    public BankingEventController(KafkaTemplate<String, Object> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    @PostMapping
//    public String publishEvent(@RequestBody Map<String, Object> payload) {
//
//        Map<String, Object> event = Map.of(
//                "eventId", UUID.randomUUID().toString(),
//                "aggregateId", "generic",
//                "eventType", "generic.event",
//                "payload", payload,
//                "timestamp", Instant.now().toString()
//        );
//
//        kafkaTemplate.send("banking.event", event);
//
//        return "sent";
//    }
//}