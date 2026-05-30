package com.teb.practice.controller;

import static java.lang.System.currentTimeMillis;

import com.teb.practice.model.MessageRequest;

import io.opentelemetry.api.trace.Tracer;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class MessageController {

    private static final String QUEUE_NAME = "TEB.MSG.QUEUE";
    private static final String MESSAGE_STATUS = "status";

    private final JmsTemplate jmsTemplate;
    private final Tracer tracer;

    @PostMapping("/send")
    public Map<String, String> send(@Valid @RequestBody MessageRequest request) {

        String requestMessage = request.message();

        var context = tracer.spanBuilder("http-send").startSpan().getSpanContext();

        jmsTemplate.convertAndSend(
                QUEUE_NAME,
                requestMessage,
                message -> {
                    message.setStringProperty("traceId", context.getTraceId());
                    message.setStringProperty("spanId", context.getSpanId());

                    message.setLongProperty("startTime", currentTimeMillis());

                    return message;
                });

        log.info(
                "Message injected | traceId: {} | spanId: {}",
                context.getTraceId(),
                context.getSpanId());

        return Map.of(MESSAGE_STATUS, "SENT", "queue", QUEUE_NAME, "message", requestMessage);
    }

    @GetMapping("/health")
    public Map<String, String> health() {

        return Map.of(MESSAGE_STATUS, "UP");
    }
}
