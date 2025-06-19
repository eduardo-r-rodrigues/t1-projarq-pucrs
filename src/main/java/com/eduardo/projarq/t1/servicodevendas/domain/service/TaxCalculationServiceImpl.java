package com.eduardo.projarq.t1.servicodevendas.domain.service;

import com.eduardo.projarq.t1.servicodevendas.domain.model.OrderItem;
import com.eduardo.projarq.t1.servicodevendas.domain.model.Product;
import com.eduardo.projarq.t1.servicodevendas.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxCalculationServiceImpl implements TaxCalculationService {
    
    private final ProductRepository productRepository;
    
    public TaxCalculationServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public double calculateStateTax(String state, List<OrderItem> items, double base) {
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
                    Product product = productRepository.findById(item.getProductCode()).orElse(null);
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

    @Override
    public double calculateFederalTax(double base) {
        return base * 0.08;
    }

    @Override
    public double calculateDiscount(List<OrderItem> items) {
        double totalQuantity = items.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
        
        if (totalQuantity >= 10) {
            return 0.10; // 10% discount
        } else if (totalQuantity >= 5) {
            return 0.05; // 5% discount
        }
        return 0.0;
    }
} 