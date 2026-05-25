package com.teb.practice.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class BusinessRoute extends RouteBuilder {

    @Override
    public void configure() {

        errorHandler(
                deadLetterChannel("direct:deadLetterRoute")
                        .maximumRedeliveries(3)
                        .redeliveryDelay(2000)
                        .useOriginalMessage()
                        .logRetryAttempted(true)
                        .retryAttemptedLogLevel(LoggingLevel.WARN)
                        .logExhausted(true));

        from("direct:businessRoute")
                .routeId("business-route")
                .log("Business process | body: ${body} | traceId: ${header.traceId}")
                .choice()

                .when(simple("${body} contains 'error'"))
                .log("Business process failure | body: ${body} | traceId: ${header.traceId}")
                .to("jms:queue:EVENT.FAILURE")
                .throwException(new RuntimeException("Business process error"))

                .otherwise()
                .log("Business process success | body: ${body} | traceId: ${header.traceId}")
                .to("jms:queue:EVENT.SUCCESS")
                .to("direct:finalRoute")
                .end();
    }
}
