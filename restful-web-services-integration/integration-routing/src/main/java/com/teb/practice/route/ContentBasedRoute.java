package com.teb.practice.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ContentBasedRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("direct:finalRoute")
                .routeId("content-based-route")

                .log("FINAL PROCESSING: ${body}");
    }
}