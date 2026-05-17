package com.teb.practice.model;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEvent {

    @Builder.Default private String transactionId = randomUUID().toString();
    @Builder.Default private LocalDateTime timestamp = now();

    private TransactionType type;
    private double amount;
}
