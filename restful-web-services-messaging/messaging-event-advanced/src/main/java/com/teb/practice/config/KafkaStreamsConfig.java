//package com.teb.practice.config;
//
//import org.apache.kafka.streams.StreamsConfig;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafkaStreams;
//import org.springframework.kafka.config.KafkaStreamsConfiguration;
//import org.springframework.kafka.support.serializer.JacksonJsonSerde;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@EnableKafkaStreams
//@Configuration
//public class KafkaStreamsConfig {
//
//    @Bean(name = "defaultKafkaStreamsConfig")
//    public KafkaStreamsConfiguration kafkaStreamsConfiguration() {
//        Map<String, Object> config = new HashMap<>();
//
//        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "messaging-event-advanced-streams");
//        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//
//        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
//                org.apache.kafka.common.serialization.Serdes.String().getClass());
//
//        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
//                JacksonJsonSerde.class);
//
//        return new KafkaStreamsConfiguration(config);
//    }
//}