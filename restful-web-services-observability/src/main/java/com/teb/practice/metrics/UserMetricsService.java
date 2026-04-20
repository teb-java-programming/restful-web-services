package com.teb.practice.metrics;

import static io.micrometer.core.instrument.Counter.builder;

import io.micrometer.core.instrument.MeterRegistry;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMetricsService {

    private final MeterRegistry meterRegistry;

    public void incrementApiCounter() {

        builder("user.api.calls")
                .description("Number of API calls")
                .register(meterRegistry)
                .increment();
    }
}
