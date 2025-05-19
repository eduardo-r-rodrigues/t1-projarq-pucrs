package com.eduardo.projarq.t1.servicodevendas.strategy;

import java.util.List;

import com.eduardo.projarq.t1.servicodevendas.model.OrderItem;
import com.eduardo.projarq.t1.servicodevendas.model.Product;
import com.eduardo.projarq.t1.servicodevendas.service.SalesService;

public class CalculateStateTaxStrategy {
    private final SalesService salesService;

    public CalculateStateTaxStrategy(SalesService salesService) {
        this.salesService = salesService;
    }

    public double calculate(String state, List<OrderItem> items, double base) {
        state = state.toUpperCase();
        switch (state) {
            case "RS":
                if (base <= 100.0) return 0.0;
                return (base - 100.0) * 0.10;
            case "SP":
                return base * 0.12;
            case "PE":
                double total = 0.0;
                for (OrderItem item : items) {
                    Product product = salesService.getProduct(item.getProductCode());
                    if (product != null && product.isEssential()) {
                        total += item.getTotalPrice() * 0.05;
                    } else {
                        total += item.getTotalPrice() * 0.15;
                    }
                }
                return total;
            default:
                return 0.0;
        }
    }
}