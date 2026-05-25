package com.teb.practice.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FinalRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("direct:finalRoute")
                .routeId("final-route")
                .log("Message processed | body: ${body} | traceId: ${header.traceId}");
    }
}
