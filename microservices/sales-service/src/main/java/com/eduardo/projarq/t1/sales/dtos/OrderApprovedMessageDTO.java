package com.eduardo.projarq.t1.sales.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderApprovedMessageDTO {
    private String orderId;
    private String customerId;
    private String state;
    private double totalAmount;
    private double taxAmount;
    private LocalDateTime approvedAt;
} 