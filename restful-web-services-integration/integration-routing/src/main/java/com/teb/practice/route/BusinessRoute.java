package com.teb.practice.route;

import static org.apache.camel.LoggingLevel.WARN;

import static java.util.Objects.nonNull;

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
                        .logRetryAttempted(true)
                        .retryAttemptedLogLevel(WARN)
                        .logExhausted(true));

        from("jms:queue:TEB.MSG.QUEUE")
                .routeId("jms-camel-route")

                // Preserve trace across JMS boundary
                .process(
                        e -> {
                            String traceId = (String) e.getMessage().getHeader("traceId");

                            if (nonNull(traceId)) {
                                e.getMessage().setHeader("JMSCorrelationID", traceId);
                                e.getMessage().setHeader("traceId", traceId);
                            }
                        })
                .log("Message received | body: ${body} | traceId: ${header.traceId}")
                .to("direct:businessRoute");

        from("direct:businessRoute")
                .routeId("business-route")
                .log(
                        "Message forwarded for processing | body: ${body} | traceId: ${header.traceId}")

                // Split message actions
                .choice()

                // Failure route
                .when(simple("${body} contains 'error'"))
                .to("jms:queue:EVENT.FAILURE")
                .log("Message not processed | body: ${body} | traceId: ${header.traceId}")
                .throwException(new RuntimeException("Business process error"))

                // Success route
                .otherwise()
                .to("jms:queue:EVENT.SUCCESS")
                .log("Message processed | body: ${body} | traceId: ${header.traceId}")
                .end();
    }
}
