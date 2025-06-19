package com.eduardo.projarq.t1.servicodevendas.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Getter
public class OrderItem {
    @Column(name = "product_code", nullable = false)
    private String productCode;
    
    @Column(nullable = false)
    private int quantity;
    
    @Column(name = "unit_price", nullable = false)
    private double unitPrice;
    
    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    public OrderItem(String productCode, int quantity, double unitPrice, double totalPrice) {
        this.productCode = productCode;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public static OrderItem create(String productCode, int quantity, double unitPrice) {
        double totalPrice = unitPrice * quantity;
        return new OrderItem(productCode, quantity, unitPrice, totalPrice);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity &&
                Double.compare(orderItem.unitPrice, unitPrice) == 0 &&
                Double.compare(orderItem.totalPrice, totalPrice) == 0 &&
                Objects.equals(productCode, orderItem.productCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, quantity, unitPrice, totalPrice);
    }
} 