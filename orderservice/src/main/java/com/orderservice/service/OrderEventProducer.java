package com.orderservice.service;

import com.orderservice.config.KafkaTopicConfig;
import com.orderservice.dto.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OrderEventProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventProducer.class);

    @Autowired
    private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public void publishOrderCreatedEvent(OrderCreatedEvent event) {
        String key = String.valueOf(event.getOrderId());
        log.info("Publishing OrderCreatedEvent to topic '{}' with key '{}': {}",
                KafkaTopicConfig.ORDER_CREATED_TOPIC, key, event);

        CompletableFuture<SendResult<String, OrderCreatedEvent>> future =
                kafkaTemplate.send(KafkaTopicConfig.ORDER_CREATED_TOPIC, key, event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Kafka publish successful for Order ID [{}]. Topic: {}, Partition: {}, Offset: {}",
                        event.getOrderId(),
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Failed to publish OrderCreatedEvent for Order ID [{}] to Kafka: {}",
                        event.getOrderId(), ex.getMessage(), ex);
            }
        });
    }
}
