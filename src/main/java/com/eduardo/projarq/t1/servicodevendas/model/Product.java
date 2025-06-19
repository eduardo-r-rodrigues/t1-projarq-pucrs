package com.eduardo.projarq.t1.servicodevendas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private String code;
    
    @Column(nullable = false)
    private String description;
    
    @Column(name = "unit_price", nullable = false)
    private double unitPrice;
    
    @Column(name = "max_stock_quantity", nullable = false)
    private int maxStockQuantity;
    
    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;
    
    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity;
    
    @Column(nullable = false)
    private boolean essential; // For Pernambuco tax rule
}
