package com.teb.practice.observability;

import lombok.RequiredArgsConstructor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventMetricsProcessor implements Processor {

    private final MessageMetrics metrics;

    @Override
    public void process(Exchange exchange) {

        switch (exchange.getFromRouteId()) {
            case "event-success" -> metrics.success();
            case "event-failure" -> metrics.failure();
            case "event-dlq" -> metrics.dlq();
            default -> {}
        }
    }
}
