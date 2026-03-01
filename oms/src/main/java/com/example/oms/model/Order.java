package com.example.oms.model;
import java.time.LocalDateTime;
import java.util.UUID;
public class Order {
    private String orderId;
    private String customerName;
    private OrderType type;
    private double amount;
    private OrderStatus status;
    private LocalDateTime createdTime;
    public Order(String customerName, OrderType type, double amount) {
        this.orderId = UUID.randomUUID().toString();
        this.customerName = customerName;
        this.type = type;
        this.amount = amount;
        this.status = OrderStatus.NEW;
        this.createdTime = LocalDateTime.now();
    }

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}