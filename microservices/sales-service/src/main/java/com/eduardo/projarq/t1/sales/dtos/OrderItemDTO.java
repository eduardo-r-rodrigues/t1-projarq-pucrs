package com.eduardo.projarq.t1.sales.dtos;

import lombok.Data;

@Data
public class OrderItemDTO {
    private String productCode;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private boolean essential;
} 