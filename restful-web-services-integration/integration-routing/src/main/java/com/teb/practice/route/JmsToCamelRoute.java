package com.teb.practice.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class JmsToCamelRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("jms:queue:TEB.MSG.QUEUE")
                .routeId("jms-to-camel-route")
                .setHeader("traceId", simple("${exchangeId}"))
                .log("Received message | body: ${body} | traceId: ${header.traceId}")
                .to("direct:processMessage");
    }
}
