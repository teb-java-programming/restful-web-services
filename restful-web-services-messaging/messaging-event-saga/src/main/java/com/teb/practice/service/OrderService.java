package com.teb.practice.service;

import static com.teb.practice.event.KafkaTopics.ORDER_CREATED;

import com.teb.practice.event.SagaEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void createOrder(SagaEvent sagaEvent) {

        kafkaTemplate.send(ORDER_CREATED, sagaEvent.getSagaId(), sagaEvent)
                .whenComplete((result, ex) -> {

                    if (ex != null) {
                        log.error("[SAGA:{}] [ORDER] publish failed",
                                sagaEvent.getSagaId(),
                                ex);
                        return;
                    }

                    var metadata = result.getRecordMetadata();

                    log.info("[SAGA:{}] [ORDER] sent partition={} offset={}",
                            sagaEvent.getSagaId(),
                            metadata.partition(),
                            metadata.offset());
                });
    }
}