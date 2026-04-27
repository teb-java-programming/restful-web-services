package com.teb.practice.controller;

import com.teb.practice.model.OrderEvent;
import com.teb.practice.service.command.OrderCommandService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderCommandService orderCommandService;

    @PostMapping
    public void createOrder(@RequestBody OrderEvent event) {

        orderCommandService.createOrder(event);
    }
}
