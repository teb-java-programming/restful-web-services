package com.teb.practice.controller;

import static java.lang.System.currentTimeMillis;
import static java.util.UUID.randomUUID;

import com.teb.practice.event.SagaEvent;
import com.teb.practice.service.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class OrderTestController {

    private final OrderService orderService;

    @PostMapping("/order")
    public String triggerOrder(@RequestHeader(value = "saga-id", required = false) String sagaId) {

        orderService.createOrder(
                SagaEvent.builder()
                        .sagaId(sagaId != null ? sagaId : "saga-" + currentTimeMillis())
                        .orderId(randomUUID().toString())
                        .currentStage("ORDER")
                        .status("ORDER_CREATED")
                        .build());

        return "Order event sent";
    }

    @PostMapping("/dlq")
    public String sendBadEvent() {

        orderService.createOrder(
                SagaEvent.builder()
                        .sagaId("saga-dlq")
                        .orderId("bad-order")
                        .currentStage("ORDER")
                        .status("INVALID_ORDER")
                        .forceFail(true)
                        .build());

        return "Order event sent to DLQ";
    }
}
