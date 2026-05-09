package com.teb.practice.event;

import static java.time.LocalDateTime.now;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SagaEvent {

    @Builder.Default private LocalDateTime eventTime = now();
    private String sagaId;
    private String orderId;
    private String currentStage;
    private String status;
    private String failAt;
}
