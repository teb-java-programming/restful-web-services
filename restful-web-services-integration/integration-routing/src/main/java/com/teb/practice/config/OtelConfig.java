package com.teb.practice.config;

import static io.opentelemetry.api.GlobalOpenTelemetry.get;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtelConfig {

    @Bean
    public Tracer tracer(OpenTelemetry openTelemetry) {

        return openTelemetry.getTracer("com-teb-practice");
    }

    @Bean
    public OpenTelemetry openTelemetry() {

        return get();
    }
}
