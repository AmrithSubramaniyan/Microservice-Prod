package com.customerservice.customerservice.consumer;

import com.customerservice.customerservice.dto.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

@Service
public class OrderCreatedEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedEventListener.class);

    @KafkaListener(
            topics = "${app.kafka.topics.order-created:order-created}",
            groupId = "${spring.kafka.consumer.group-id:customer-service-group}"
    )
    public void handleOrderCreatedEvent(
            @Payload OrderCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("================================================================================");
        log.info("Received OrderCreatedEvent from Kafka!");
        log.info("Topic: order-created | Partition: {} | Offset: {} | Key: {}", partition, offset, key);
        log.info("Order Details -> ID: {}, Customer ID: {}, Product: {}, Quantity: {}, Total: ${}, Status: {}",
                event.getOrderId(),
                event.getCustomerId(),
                event.getName(),
                event.getQuantity(),
                event.getTotalAmount(),
                event.getStatus());

        // Simulate customer notification action (e.g., SMS, Email, Push Notification)
        simulateCustomerNotification(event);
        log.info("================================================================================");
    }

    private void simulateCustomerNotification(OrderCreatedEvent event) {
        log.info(">>> [SIMULATED NOTIFICATION DISPATCHED] Sending Order Confirmation Email/SMS to Customer ID: {} for Order #{} (Item: '{}', Amount: ${})",
                event.getCustomerId(),
                event.getOrderId(),
                event.getName(),
                event.getTotalAmount());
    }
}
