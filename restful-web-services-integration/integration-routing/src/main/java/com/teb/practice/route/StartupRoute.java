package com.teb.practice.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class StartupRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("timer:start?repeatCount=1")
                .routeId("startup-route")
                .log("Camel routing engine started");
    }
}
