package com.teb.practice.aggregation;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class MessageAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        Boolean success =
                newExchange.getMessage().getHeader("isSuccess", Boolean.class);

        if (success == null || !success) {
            return oldExchange; // ignore failed messages
        }

        if (oldExchange == null) {
            return newExchange;
        }

        String oldBody =
                oldExchange.getMessage().getBody(String.class);

        String newBody =
                newExchange.getMessage().getBody(String.class);

        oldExchange.getMessage().setBody(oldBody + " | " + newBody);

        return oldExchange;
    }
}