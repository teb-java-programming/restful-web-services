package com.teb.practice.rabbitmq.producer;

import static com.teb.practice.constants.MessagingConstants.MESSAGING_BASIC_EXCHANGE;
import static com.teb.practice.constants.MessagingConstants.ROUTING_KEY;

import com.teb.practice.model.EventMessage;
import com.teb.practice.util.JsonUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final JsonUtil jsonUtil;

    public void send(EventMessage eventMessage) {

        rabbitTemplate.convertAndSend(
                MESSAGING_BASIC_EXCHANGE, ROUTING_KEY, jsonUtil.toJson(eventMessage));
    }
}
