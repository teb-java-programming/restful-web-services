package com.teb.practice.service;

import com.teb.practice.model.OrderEvent;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService {

    public void process(OrderEvent order) {

        log.info("Payment processed for: {}", order.getOrderId());
    }

    public void refund(OrderEvent order) {

        log.info("Payment refunded for: {}", order.getOrderId());
    }
}
