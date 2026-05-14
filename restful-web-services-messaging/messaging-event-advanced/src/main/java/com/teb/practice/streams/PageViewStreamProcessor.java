//package com.teb.practice.streams;
//
//import static com.teb.practice.kafka.topics.KafkaTopics.PAGE_VIEWS;
//import static com.teb.practice.kafka.topics.KafkaTopics.PAGE_VIEWS_VALID;
//import static org.apache.kafka.streams.kstream.Consumed.with;
//
//import com.teb.practice.events.PageViewEvent;
//
//import org.apache.kafka.common.serialization.Serdes;
//import org.apache.kafka.streams.StreamsBuilder;
//import org.apache.kafka.streams.kstream.KStream;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//@Component
//public class PageViewStreamProcessor {
//
//    @Bean
//    public KStream<String, PageViewEvent> process(StreamsBuilder builder) {
//
//        KStream<String, PageViewEvent> stream =
//                builder.stream(PAGE_VIEWS, with(Serdes.String(), null));
//
//        stream.filter(this::isValid)
//                .to(PAGE_VIEWS_VALID);
//
//        return stream;
//    }
//
//    private boolean isValid(String key, PageViewEvent event) {
//
//        return event != null
//                && event.getUserId() != null
//                && event.getPage() != null
//                && event.getDurationSeconds() > 0;
//    }
//}