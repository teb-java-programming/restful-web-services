package com.teb.practice.route;

import com.teb.practice.observability.EventMetricsProcessor;

import lombok.RequiredArgsConstructor;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventMetricsRoute extends RouteBuilder {

    private final EventMetricsProcessor processor;

    @Override
    public void configure() {

        from("jms:queue:EVENT.SUCCESS")
                .routeId("event-success")
                .log("Event success: ${body}")
                .process(processor);

        from("jms:queue:EVENT.FAILURE")
                .routeId("event-failure")
                .log("Event failure: ${body}")
                .process(processor);

        from("jms:queue:EVENT.DLQ")
                .routeId("event-dlq")
                .log("Event dlq: ${body}")
                .process(processor);
    }
}