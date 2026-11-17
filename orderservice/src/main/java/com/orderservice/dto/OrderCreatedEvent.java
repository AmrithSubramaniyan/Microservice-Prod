package com.orderservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OrderCreatedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long orderId;
    private Long customerId;
    private String name;
    private Double price;
    private Integer quantity;
    private Double totalAmount;
    private String status;
    private LocalDateTime timestamp;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(Long orderId, Long customerId, String name, Double price, Integer quantity, Double totalAmount, String status, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.status = status;
        this.timestamp = timestamp;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "OrderCreatedEvent{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
