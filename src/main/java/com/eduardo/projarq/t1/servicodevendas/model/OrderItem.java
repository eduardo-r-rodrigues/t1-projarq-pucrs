package com.eduardo.projarq.t1.servicodevendas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String productCode;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
} 