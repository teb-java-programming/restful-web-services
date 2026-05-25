package com.teb.practice.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.stereotype.Component;

@Component
public class MessageMetrics {

    private final Counter success;
    private final Counter failure;
    private final Counter dlq;

    public MessageMetrics(MeterRegistry registry) {

        this.success = registry.counter("app.messages.success");
        this.failure = registry.counter("app.messages.failure");
        this.dlq = registry.counter("app.messages.dlq");
    }

    public void success() {

        success.increment();
    }

    public void failure() {

        failure.increment();
    }

    public void dlq() {

        dlq.increment();
    }
}
