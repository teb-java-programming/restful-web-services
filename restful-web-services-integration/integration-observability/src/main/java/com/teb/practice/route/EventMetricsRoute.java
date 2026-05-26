package com.teb.practice.route;

import static java.lang.System.currentTimeMillis;

import com.teb.practice.observability.EventMetricsProcessor;

import lombok.RequiredArgsConstructor;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventMetricsRoute extends RouteBuilder {

    private final EventMetricsProcessor eventMetricsProcessor;

    @Override
    public void configure() {

        from("jms:queue:EVENT.SUCCESS")
                .routeId("event-success")
                .process(
                        e ->
                                e.getMessage()
                                        .setHeader(
                                                "duration",
                                                currentTimeMillis()
                                                        - e.getMessage()
                                                                .getHeader(
                                                                        "startTime", Long.class)))
                .log("SUCCESS: ${body} | traceId=${header.traceId} | duration=${header.duration}")
                .process(eventMetricsProcessor);

        from("jms:queue:EVENT.FAILURE")
                .routeId("event-failure")
                .process(
                        e ->
                                e.getMessage()
                                        .setHeader(
                                                "duration",
                                                currentTimeMillis()
                                                        - e.getMessage()
                                                                .getHeader(
                                                                        "startTime", Long.class)))
                .log("FAILURE: ${body} | traceId=${header.traceId} | duration=${header.duration}")
                .process(eventMetricsProcessor);

        from("jms:queue:EVENT.DLQ")
                .routeId("event-dlq")
                .process(
                        e ->
                                e.getMessage()
                                        .setHeader(
                                                "duration",
                                                currentTimeMillis()
                                                        - e.getMessage()
                                                                .getHeader(
                                                                        "startTime", Long.class)))
                .log("DLQ: ${body} | traceId=${header.traceId} | duration=${header.duration}")
                .process(eventMetricsProcessor);
    }
}
