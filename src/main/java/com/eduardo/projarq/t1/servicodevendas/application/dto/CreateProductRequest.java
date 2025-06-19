package com.eduardo.projarq.t1.servicodevendas.application.dto;

import lombok.Data;

@Data
public class CreateProductRequest {
    private String code;
    private String description;
    private double unitPrice;
    private int maxStockQuantity;
    private boolean essential;
} 