package com.teb.practice.route;

import static io.opentelemetry.api.trace.StatusCode.ERROR;

import static org.apache.camel.LoggingLevel.WARN;

import static java.util.Objects.nonNull;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

import lombok.RequiredArgsConstructor;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BusinessRoute extends RouteBuilder {

    private static final String SPAN = "span";
    private static final String TRACE_ID = "traceId";
    private static final String SPAN_ID = "spanId";

    private final Tracer tracer;

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

                // Extract trace context from JMS headers
                .process(
                        exchange -> {
                            var traceId = exchange.getMessage().getHeader(TRACE_ID, String.class);
                            var spanId = exchange.getMessage().getHeader(SPAN_ID, String.class);

                            if (nonNull(traceId) && nonNull(spanId)) {
                                exchange.setProperty(TRACE_ID, traceId);
                                exchange.setProperty(SPAN_ID, spanId);
                            }
                        })
                .log(
                        "Message received | body: ${body} | traceId: ${header.traceId} | spanId: ${header.spanId}")
                .to("direct:businessRoute");

        from("direct:businessRoute")
                .routeId("business-route")

                // Start Otel span
                .process(
                        exchange -> {
                            var span = tracer.spanBuilder("business-route").startSpan();
                            span.makeCurrent();
                            exchange.setProperty(SPAN, span);
                        })
                .log(
                        "Message forwarded for processing | body: ${body} | traceId: ${header.traceId}")

                // Split message actions
                .choice()

                // Failure route
                .when(simple("${body} contains 'error'"))
                .process(
                        exchange -> {
                            var span = exchange.getProperty(SPAN, Span.class);
                            if (nonNull(span)) {
                                span.recordException(
                                        new RuntimeException("Business process error"));
                                span.setStatus(ERROR);
                            }
                        })
                .to("jms:queue:EVENT.FAILURE")
                .log("Message not processed | body: ${body} | traceId: ${header.traceId}")
                .throwException(new RuntimeException("Business process error"))

                // Success route
                .otherwise()
                .to("jms:queue:EVENT.SUCCESS")
                .log("Message processed | body: ${body} | traceId: ${header.traceId}")
                .end()

                // Close span
                .process(
                        exchange -> {
                            var span = exchange.getProperty(SPAN, Span.class);
                            if (nonNull(span)) {
                                span.end();
                            }
                        });
    }
}
