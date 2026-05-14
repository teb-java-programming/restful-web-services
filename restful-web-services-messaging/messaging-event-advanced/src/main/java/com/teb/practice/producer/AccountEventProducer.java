//package com.teb.practice.producer;
//
//import static com.teb.practice.kafka.KafkaTopics.ACCOUNT_CREATED;
//
//import com.teb.practice.events.EventEnvelope;
//
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class AccountEventProducer {
//
//    private final KafkaTemplate<String, Object> kafkaTemplate;
//
//    public void publish(String key, EventEnvelope<?> event) {
//
//        kafkaTemplate.send(ACCOUNT_CREATED, key, event);
//    }
//}
