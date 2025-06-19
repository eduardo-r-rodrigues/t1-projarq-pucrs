package com.eduardo.projarq.t1.servicodevendas.application.dto;

import lombok.Data;

@Data
public class OrderItemRequest {
    private String productCode;
    private int quantity;
} 