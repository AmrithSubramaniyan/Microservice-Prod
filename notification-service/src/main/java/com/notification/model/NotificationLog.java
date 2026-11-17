package com.notification.model;

import java.time.LocalDateTime;

public class NotificationLog {
    private String id;
    private Long orderId;
    private Long customerId;
    private String message;
    private Double amount;
    private String status;
    private LocalDateTime sentAt;

    public NotificationLog() {
    }

    public NotificationLog(String id, Long orderId, Long customerId, String message, Double amount, String status, LocalDateTime sentAt) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.message = message;
        this.amount = amount;
        this.status = status;
        this.sentAt = sentAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}

