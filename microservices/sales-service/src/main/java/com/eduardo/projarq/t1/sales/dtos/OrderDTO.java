package com.eduardo.projarq.t1.sales.dtos;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private String id;
    private String customerId;
    private String state;
    private String country;
    private List<OrderItemDTO> items;
    private double totalAmount;
    private double taxAmount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
} 