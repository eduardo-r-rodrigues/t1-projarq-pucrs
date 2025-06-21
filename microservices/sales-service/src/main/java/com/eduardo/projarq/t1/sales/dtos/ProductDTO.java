package com.eduardo.projarq.t1.sales.dtos;

import lombok.Data;

@Data
public class ProductDTO {
    private String code;
    private String name;
    private String description;
    private double price;
    private int stock;
    private boolean essential;
} 