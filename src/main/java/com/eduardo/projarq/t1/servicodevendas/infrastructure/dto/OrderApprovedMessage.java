package com.eduardo.projarq.t1.servicodevendas.infrastructure.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderApprovedMessage {
    private String orderId;
    private String customerId;
    private String state;
    private double totalAmount;
    private double taxAmount;
    private LocalDateTime approvedAt;
} 