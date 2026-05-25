package com.teb.practice.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("direct:deadLetterRoute")
                .routeId("dead-letter-route")
                .setHeader("traceId", simple("${exchangeId}"))

                .log("ENTER DLQ: ${body}")
                .log("DLQ ENTRY | traceId=${header.traceId} | body=${body}")
                .to("jms:queue:EVENT.DLQ");
    }
}