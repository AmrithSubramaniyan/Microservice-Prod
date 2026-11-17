package com.notification.controller;

import com.notification.model.NotificationLog;
import com.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationLog>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<NotificationLog>> getCustomerNotifications(@PathVariable Long customerId) {
        return ResponseEntity.ok(notificationService.getNotificationsByCustomerId(customerId));
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        return ResponseEntity.ok(Map.of(
                "service", "notification-service",
                "status", "UP",
                "totalDispatched", notificationService.getAllNotifications().size()
        ));
    }
}

