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
                        .logExhausted(true)
        );

        from("direct:businessRoute")
                .routeId("business-route")

                // ✔ Redelivery tracking
                .log("BUSINESS ROUTE processing: ${body}")
                .log("Exchange ID: ${exchangeId}")
                .log("TRACE CHECK | traceId=${header.traceId}")
                .choice()
                .when(simple("${body} contains 'error'"))
                .log("Business failure triggered")
                .to("jms:queue:EVENT.FAILURE")
                .throwException(new RuntimeException("Business error"))
                .otherwise()
                .log("Business success: ${body}")
                .to("jms:queue:EVENT.SUCCESS")
                .to("direct:finalRoute")
                .end();
    }
}