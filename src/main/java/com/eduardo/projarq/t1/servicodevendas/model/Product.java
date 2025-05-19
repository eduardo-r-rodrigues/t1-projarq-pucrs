package com.eduardo.projarq.t1.servicodevendas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String code;
    private String description;
    private double unitPrice;
    private int maxStockQuantity;
    private int stockQuantity;
    private int availableQuantity;
    private boolean essential; // For Pernambuco tax rule
}
