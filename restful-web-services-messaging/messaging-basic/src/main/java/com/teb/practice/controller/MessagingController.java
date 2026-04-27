package com.teb.practice.controller;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

import com.teb.practice.kafka.producer.KafkaProducerService;
import com.teb.practice.model.EventMessage;
import com.teb.practice.rabbitmq.producer.RabbitProducerService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messaging")
@RequiredArgsConstructor
public class MessagingController {

    private final KafkaProducerService kafkaProducerService;
    private final RabbitProducerService rabbitProducerService;

    @PostMapping("/kafka")
    public String sendKafka(
            @RequestParam(required = false) String id, @RequestParam String message) {

        EventMessage eventMessage =
                EventMessage.builder()
                        .id(id != null ? id : randomUUID().toString())
                        .message(message)
                        .timestamp(now())
                        .build();

        kafkaProducerService.send(eventMessage);

        return "Kafka message sent";
    }

    @PostMapping("/rabbit")
    public String sendRabbit(
            @RequestParam(required = false) String id, @RequestParam String message) {

        EventMessage eventMessage =
                EventMessage.builder()
                        .id(id != null ? id : randomUUID().toString())
                        .message(message)
                        .timestamp(now())
                        .build();

        rabbitProducerService.send(eventMessage);

        return "RabbitMQ message sent";
    }
}
