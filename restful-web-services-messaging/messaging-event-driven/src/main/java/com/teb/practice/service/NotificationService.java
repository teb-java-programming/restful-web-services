package com.teb.practice.service;

import com.teb.practice.model.OrderEvent;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void notifySuccess(OrderEvent order) {

        log.info("Success notification: {}", order.getOrderId());
    }

    public void notifyFailure(OrderEvent order) {

        log.info("Failure notification: {}", order.getOrderId());
    }
}
