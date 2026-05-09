package com.teb.practice.config;

import static java.util.Optional.ofNullable;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@Slf4j
public class KafkaConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            KafkaTemplate<String, Object> kafkaTemplate) {

        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler(kafkaTemplate));

        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, Object> kafkaTemplate) {

        DefaultErrorHandler errorHandler =
                new DefaultErrorHandler(
                        new DeadLetterPublishingRecoverer(
                                kafkaTemplate,
                                (record, _) ->
                                        new TopicPartition(
                                                record.topic() + ".dlq", record.partition())),
                        new FixedBackOff(100L, 3));

        errorHandler.setRetryListeners(
                (record, e, attempt) ->
                        log.warn(
                                "[{}] RETRY attempt={} topic={} offset={} reason={}",
                                record.key(),
                                attempt,
                                record.topic(),
                                record.offset(),
                                ofNullable(e).map(Throwable::getMessage).orElse("UNKNOWN")));

        return errorHandler;
    }
}
