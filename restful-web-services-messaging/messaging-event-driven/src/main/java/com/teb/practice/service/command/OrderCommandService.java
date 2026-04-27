package com.teb.practice.service.command;

import com.teb.practice.model.OrderEvent;
import com.teb.practice.service.OutboxService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderCommandService {

    private final OutboxService outboxService;

    public void createOrder(OrderEvent event) {

        outboxService.save(event.getOrderId(), "ORDER_CREATED", event);
    }
}
