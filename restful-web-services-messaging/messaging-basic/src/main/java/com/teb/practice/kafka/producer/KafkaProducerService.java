package com.teb.practice.kafka.producer;

import static com.teb.practice.constants.MessagingConstants.MESSAGING_BASIC_TOPIC;

import com.teb.practice.model.EventMessage;
import com.teb.practice.util.JsonUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonUtil jsonUtil;

    public void send(EventMessage eventMessage) {

        kafkaTemplate.send(
                MESSAGING_BASIC_TOPIC, eventMessage.getId(), jsonUtil.toJson(eventMessage));
    }
}
