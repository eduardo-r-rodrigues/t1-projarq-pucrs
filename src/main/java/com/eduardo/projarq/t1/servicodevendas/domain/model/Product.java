package com.eduardo.projarq.t1.servicodevendas.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "products")
@NoArgsConstructor
@Getter
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

    public Product(String code, String description, double unitPrice, int maxStockQuantity, boolean essential) {
        this.code = code;
        this.description = description;
        this.unitPrice = unitPrice;
        this.maxStockQuantity = maxStockQuantity;
        this.stockQuantity = 0;
        this.availableQuantity = 0;
        this.essential = essential;
    }

    // Domain methods
    public boolean hasAvailableStock(int quantity) {
        return availableQuantity >= quantity;
    }

    public void reserveStock(int quantity) {
        if (!hasAvailableStock(quantity)) {
            throw new IllegalStateException("Insufficient stock for product: " + code);
        }
        availableQuantity -= quantity;
        stockQuantity -= quantity;
    }

    public void addStock(int quantity) {
        int newStock = Math.min(stockQuantity + quantity, maxStockQuantity);
        int addedQuantity = newStock - stockQuantity;
        stockQuantity = newStock;
        availableQuantity += addedQuantity;
    }

    public double calculateTotalPrice(int quantity) {
        return unitPrice * quantity;
    }

    public boolean isEssential() {
        return essential;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(code, product.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
} 