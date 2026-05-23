package com.teb.practice.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class JmsToCamelRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("jms:queue:DEV.QUEUE.1")
                .routeId("jms-to-camel-route")

                .log("ENTER route=jms-to-camel | body=${body}")

                .log("STEP 1: consumed from DEV.QUEUE.1")

                .to("direct:processMessage");
    }
}