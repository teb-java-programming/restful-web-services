package com.teb.practice.route;

import com.teb.practice.aggregation.MessageAggregationStrategy;
import com.teb.practice.common.LogHelper;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MessageRoute extends RouteBuilder {

    private static final Logger log =
            LoggerFactory.getLogger(MessageRoute.class);

    private final MessageAggregationStrategy aggregationStrategy;

    public MessageRoute(MessageAggregationStrategy aggregationStrategy) {
        this.aggregationStrategy = aggregationStrategy;
    }

    @Override
    public void configure() {

        // -----------------------------
        // GLOBAL ERROR HANDLING
        // -----------------------------
        onException(RuntimeException.class)
                .handled(true)
                .process(exchange -> {
                    String correlationId =
                            exchange.getMessage()
                                    .getHeader("correlationId", String.class);

                    String body =
                            exchange.getMessage().getBody(String.class);

                    LogHelper.log(log,
                            "ERROR",
                            correlationId,
                            body + " | " + exchange.getProperty("CamelExceptionCaught"));
                })
                .to("jms:queue:DEV.ERROR.QUEUE");

        // -----------------------------
        // MAIN ROUTE
        // -----------------------------
        from("direct:process-message")
                .routeId("message-route")

                // correlation setup
                .process(exchange -> {
                    String correlationId =
                            exchange.getMessage()
                                    .getHeader("correlationId", String.class);

                    if (correlationId == null) {
                        correlationId = UUID.randomUUID().toString();
                    }

                    exchange.getMessage().setHeader("correlationId", correlationId);

                    LogHelper.log(log,
                            "START",
                            correlationId,
                            exchange.getMessage().getBody(String.class));
                })

                // -----------------------------
                // SPLIT + PROCESS + AGGREGATE
                // -----------------------------
                .split(body().tokenize(","), aggregationStrategy)

                .process(exchange -> {

                    String correlationId =
                            exchange.getMessage()
                                    .getHeader("correlationId", String.class);

                    String body = exchange.getMessage()
                            .getBody(String.class)
                            .trim()
                            .toUpperCase();

                    exchange.getMessage().setBody(body);

                    LogHelper.log(log,
                            "SPLIT",
                            correlationId,
                            body);

                    if (body.contains("ERROR")) {
                        throw new RuntimeException("Simulated processing failure");
                    }
                })

                .to("jms:queue:DEV.QUEUE.1")

                .end()

                // final aggregated output
                .process(exchange -> {
                    String correlationId =
                            exchange.getMessage()
                                    .getHeader("correlationId", String.class);

                    String body =
                            exchange.getMessage().getBody(String.class);

                    LogHelper.log(log,
                            "END",
                            correlationId,
                            body);
                });
    }
}