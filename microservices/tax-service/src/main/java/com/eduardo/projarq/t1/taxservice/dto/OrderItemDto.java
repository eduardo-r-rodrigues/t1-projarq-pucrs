package com.eduardo.projarq.t1.taxservice.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private String productCode;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private boolean essential;
} 