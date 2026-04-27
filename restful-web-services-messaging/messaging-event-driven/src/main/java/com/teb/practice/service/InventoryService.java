package com.teb.practice.service;

import com.teb.practice.model.OrderEvent;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InventoryService {

    public void reserve(OrderEvent order) {

        log.info("Inventory reserved for {}", order.getOrderId());
    }

    public void release(OrderEvent order) {

        log.info("Inventory released for {}", order.getOrderId());
    }
}
