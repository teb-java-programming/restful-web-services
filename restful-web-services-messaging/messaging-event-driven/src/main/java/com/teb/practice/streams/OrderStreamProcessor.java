package com.teb.practice.streams;

import static com.teb.practice.constants.TopicConstants.ORDER_CREATED;
import static com.teb.practice.constants.TopicConstants.PAYMENT_PROCESSED;

import static org.apache.kafka.streams.kstream.Consumed.with;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teb.practice.model.OrderEvent;

import lombok.RequiredArgsConstructor;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderStreamProcessor {

    private final ObjectMapper objectMapper;

    @Bean
    public KStream<String, String> buildTopology(StreamsBuilder builder) {

        KStream<String, String> stream =
                builder.stream(ORDER_CREATED, with(Serdes.String(), Serdes.String()));

        stream.filter((key, value) -> isCreated(value)).to(PAYMENT_PROCESSED);

        return stream;
    }

    private boolean isCreated(String value) {

        try {
            return "CREATED".equals(objectMapper.readValue(value, OrderEvent.class).getStatus());
        } catch (Exception e) {
            return false;
        }
    }
}
