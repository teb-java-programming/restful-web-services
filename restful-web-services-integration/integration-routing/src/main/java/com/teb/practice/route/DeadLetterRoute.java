package com.teb.practice.route;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("direct:deadLetterRoute")
                .routeId("dead-letter-route")
                .log("ENTER DLQ: ${body}")

                .log("Message moved to dead letter flow: ${body}");
    }
}