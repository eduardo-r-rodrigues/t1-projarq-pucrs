package com.eduardo.projarq.t1.taxservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class TaxCalculationRequest {
    private String state;
    private List<OrderItemDto> items;
    private double baseAmount;
}

@Data
class OrderItemDto {
    private String productCode;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private boolean essential;
} 