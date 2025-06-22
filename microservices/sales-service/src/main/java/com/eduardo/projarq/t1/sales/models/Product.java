package com.eduardo.projarq.t1.sales.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
@Data
@NoArgsConstructor
public class Product {
    @Id
    private String id;
    
    private String code;
    
    private String name;
    
    private String description;
    
    private double price;
    
    private int stock;
    
    private boolean essential;
} 