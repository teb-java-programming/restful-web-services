//package com.teb.practice.consumer;
//
//import com.teb.practice.events.AccountCreatedEvent;
//import com.teb.practice.events.EventEnvelope;
//
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class AccountEventConsumer {
//
//    @KafkaListener(topics = "banking.account.created", groupId = "messaging-event-advanced-group")
//    public void consume(EventEnvelope<AccountCreatedEvent> event) {
//
//        log.info("Received event: {}", event);
//    }
//}
