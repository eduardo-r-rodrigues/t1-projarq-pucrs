package com.eduardo.projarq.t1.servicodevendas.strategy;

import java.util.List;

import com.eduardo.projarq.t1.servicodevendas.model.OrderItem;

public class CalculateDiscountStrategy {
    public double calculate(List<OrderItem> items) {
        int totalItems = items.stream().mapToInt(OrderItem::getQuantity).sum();
        double subtotal = items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
        if (totalItems > 10) {
            return subtotal * 0.10;
        } else if (totalItems > 3) {
            return subtotal * 0.05;
        }

        return 0.0;
    }
}
