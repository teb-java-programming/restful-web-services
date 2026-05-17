package com.teb.practice.controller;

import com.teb.practice.model.TransactionRequest;
import com.teb.practice.model.TransactionResponse;

import lombok.RequiredArgsConstructor;

import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final ProducerTemplate producerTemplate;

    @PostMapping
    public TransactionResponse process(@RequestBody TransactionRequest request) {

        return producerTemplate.requestBody(
                "direct:transaction", request, TransactionResponse.class);
    }
}
