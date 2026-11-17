package com.notification.service;

import com.notification.dto.OrderPurchasedEvent;
import com.notification.model.NotificationLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final List<NotificationLog> notifications = Collections.synchronizedList(new ArrayList<>());

    public NotificationLog processOrderPurchase(OrderPurchasedEvent event) {
        String msg = String.format("Order #%d confirmed! Item: %s (Qty: %d, Total: $%.2f). Confirmation sent to Customer #%d.",
                event.getOrderId(),
                event.getName(),
                event.getQuantity(),
                event.getTotalAmount(),
                event.getCustomerId());

        log.info("================================================================================");
        log.info("[NOTIFICATION SERVICE] PROCESSING ORDER PURCHASE FOR CUSTOMER #{}", event.getCustomerId());
        log.info("Sending simulated SMS/Email to Customer #{} for Order #{}...", event.getCustomerId(), event.getOrderId());
        log.info("Notification Message: {}", msg);
        log.info("================================================================================");

        NotificationLog record = new NotificationLog(
                UUID.randomUUID().toString(),
                event.getOrderId(),
                event.getCustomerId(),
                msg,
                event.getTotalAmount(),
                "DELIVERED",
                LocalDateTime.now()
        );

        notifications.add(record);
        return record;
    }

    public List<NotificationLog> getAllNotifications() {
        return new ArrayList<>(notifications);
    }

    public List<NotificationLog> getNotificationsByCustomerId(Long customerId) {
        return notifications.stream()
                .filter(n -> customerId != null && customerId.equals(n.getCustomerId()))
                .collect(Collectors.toList());
    }
}

