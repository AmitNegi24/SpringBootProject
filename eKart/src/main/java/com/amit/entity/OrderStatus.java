package com.amit.entity;

public enum OrderStatus {
    PENDING,   // Order has been placed but not yet processed
    SHIPPED,   // Order has been shipped
    DELIVERED, // Order has been delivered
    CANCELLED  // Order was cancelled
}
