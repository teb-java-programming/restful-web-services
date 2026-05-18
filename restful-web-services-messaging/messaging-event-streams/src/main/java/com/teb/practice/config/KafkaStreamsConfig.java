package com.teb.practice.config;

import static com.teb.practice.config.KafkaTopicConfig.RIDE_EVENTS_LIFECYCLE_TOPIC;
import static com.teb.practice.config.KafkaTopicConfig.RIDE_EVENTS_PROCESSED_TOPIC;

import static org.apache.kafka.streams.kstream.Consumed.with;

import static java.util.concurrent.ConcurrentHashMap.newKeySet;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.util.Set;

@Configuration
@EnableKafkaStreams
@Slf4j
public class KafkaStreamsConfig {

    private static final Set<String> CANCELLED_RIDES = newKeySet();

    @Bean
    public KStream<String, String> rideTopology(StreamsBuilder builder) {

        KStream<String, String> stream =
                builder.stream(RIDE_EVENTS_LIFECYCLE_TOPIC, with(Serdes.String(), Serdes.String()));

        stream.peek(
                        (key, value) -> {
                            if (value != null && value.contains("\"eventType\":\"CANCELLED\"")) {
                                CANCELLED_RIDES.add(key);
                            }
                            log.info("Streaming in: {} -> {}", key, value);
                        })
                .filter((key, value) -> value != null && !CANCELLED_RIDES.contains(key))
                .to(RIDE_EVENTS_PROCESSED_TOPIC);

        return stream;
    }
}
