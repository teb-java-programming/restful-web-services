package com.teb.practice.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EntryRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("direct:processMessage")
                .routeId("entry-route")
                .log("Message routed | body: ${body} | traceId: ${header.traceId}")
                .to("direct:businessRoute");
    }
}
