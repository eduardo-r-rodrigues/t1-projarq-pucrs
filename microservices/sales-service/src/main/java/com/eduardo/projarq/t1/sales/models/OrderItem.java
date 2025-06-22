package com.eduardo.projarq.t1.sales.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItem {
    private String productCode;
    
    private int quantity;
    
    private double unitPrice;
    
    private double totalPrice;
    
    private boolean essential;
} 