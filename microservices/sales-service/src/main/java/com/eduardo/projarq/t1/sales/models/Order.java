package com.eduardo.projarq.t1.sales.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
@Data
@NoArgsConstructor
public class Order {
    @Id
    private String id;
    
    private String orderId;
    
    private String customerId;
    
    private String state;
    
    private String country;
    
    private double totalAmount;
    
    private double taxAmount;
    
    private OrderStatus status;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime approvedAt;
    
    private List<OrderItem> items;
    
    public enum OrderStatus {
        PENDING, APPROVED, CANCELLED
    }
} 