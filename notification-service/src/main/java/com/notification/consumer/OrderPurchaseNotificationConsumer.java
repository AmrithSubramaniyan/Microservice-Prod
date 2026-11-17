package com.notification.consumer;

import com.notification.dto.OrderPurchasedEvent;
import com.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class OrderPurchaseNotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderPurchaseNotificationConsumer.class);

    private final NotificationService notificationService;

    public OrderPurchaseNotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.order-created:order-created}",
            groupId = "${spring.kafka.consumer.group-id:notification-service-group}"
    )
    public void handleOrderPurchased(
            @Payload OrderPurchasedEvent event,
            @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) String key,
            @Header(value = KafkaHeaders.RECEIVED_PARTITION, required = false) Integer partition,
            @Header(value = KafkaHeaders.OFFSET, required = false) Long offset) {

        log.info("Received Kafka Event -> Topic: order-created | Partition: {} | Offset: {} | OrderId: {}",
                partition, offset, event.getOrderId());

        notificationService.processOrderPurchase(event);
    }
}

