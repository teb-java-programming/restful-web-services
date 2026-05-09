package com.teb.practice.controller;

import static java.lang.System.currentTimeMillis;
import static java.util.UUID.randomUUID;

import com.teb.practice.event.SagaEvent;
import com.teb.practice.service.OrderService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public String triggerOrder(
            @Valid @RequestBody SagaEvent sagaEvent,
            @RequestHeader(value = "sagaId", required = false) String sagaId) {

        orderService.createOrder(
                SagaEvent.builder()
                        .sagaId(sagaId != null ? sagaId : "saga-" + currentTimeMillis())
                        .orderId(randomUUID().toString())
                        .currentStage(sagaEvent.getCurrentStage())
                        .status(sagaEvent.getStatus())
                        .build());

        return "Order event sent";
    }

    @PostMapping("/dlq")
    public String triggerDlqOrder(@Valid @RequestBody SagaEvent sagaEvent) {

        orderService.createOrder(
                SagaEvent.builder()
                        .sagaId("saga-dlq-" + currentTimeMillis())
                        .orderId("dlq-" + randomUUID())
                        .currentStage(sagaEvent.getCurrentStage())
                        .status(sagaEvent.getStatus())
                        .failAt(sagaEvent.getFailAt())
                        .build());

        return "Order event sent to DLQ";
    }
}
