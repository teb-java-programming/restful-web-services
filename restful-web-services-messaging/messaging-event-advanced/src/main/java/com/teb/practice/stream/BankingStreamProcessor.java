//package com.teb.practice.stream;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.common.serialization.Serdes;
//import org.apache.kafka.streams.StreamsBuilder;
//import org.apache.kafka.streams.kstream.KStream;
//import org.apache.kafka.streams.kstream.KTable;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class BankingStreamProcessor {
//
//    @Bean
//    public KStream<String, String> process(StreamsBuilder builder) {
//
//        // raw stream
//        KStream<String, String> raw = builder.stream(
//                "banking.event",
//                org.apache.kafka.streams.kstream.Consumed.with(
//                        Serdes.String(),
//                        Serdes.String()
//                )
//        );
//
//        // processed stream
//        KStream<String, String> processed = raw
//                .peek((k, v) -> log.info("raw: {}", v))
//                .mapValues(v -> v + "|processed");
//
//        processed.to(
//                "banking.event.processed",
//                org.apache.kafka.streams.kstream.Produced.with(
//                        Serdes.String(),
//                        Serdes.String()
//                )
//        );
//
//        // enriched stream
//        KStream<String, String> enriched = builder.stream(
//                "banking.event.processed",
//                org.apache.kafka.streams.kstream.Consumed.with(
//                        Serdes.String(),
//                        Serdes.String()
//                )
//        ).mapValues(v -> v + "|enriched");
//
//        enriched.to(
//                "banking.event.enriched",
//                org.apache.kafka.streams.kstream.Produced.with(
//                        Serdes.String(),
//                        Serdes.String()
//                )
//        );
//
//        // stateful aggregation
//        KTable<String, Long> counts = builder
//                .stream(
//                        "banking.event",
//                        org.apache.kafka.streams.kstream.Consumed.with(
//                                Serdes.String(),
//                                Serdes.String()
//                        )
//                )
//                .groupByKey()
//                .count();
//
//        counts.toStream()
//                .mapValues(String::valueOf)
//                .to(
//                        "banking.event.aggregated",
//                        org.apache.kafka.streams.kstream.Produced.with(
//                                Serdes.String(),
//                                Serdes.String()
//                        )
//                );
//
//        return processed;
//    }
//}