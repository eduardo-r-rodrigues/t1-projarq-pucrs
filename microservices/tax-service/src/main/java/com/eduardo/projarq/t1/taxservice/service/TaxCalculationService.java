package com.eduardo.projarq.t1.taxservice.service;

import com.eduardo.projarq.t1.taxservice.dto.TaxCalculationRequest;
import com.eduardo.projarq.t1.taxservice.dto.TaxCalculationResponse;
import com.eduardo.projarq.t1.taxservice.dto.OrderItemDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxCalculationService {

    public TaxCalculationResponse calculateTaxes(TaxCalculationRequest request) {
        TaxCalculationResponse response = new TaxCalculationResponse();
        
        double discount = calculateDiscount(request.getItems());
        response.setDiscount(discount);

        double baseForTaxes = request.getBaseAmount() - discount;
        double stateTax = calculateStateTax(request.getState(), request.getItems(), baseForTaxes);
        double federalTax = calculateFederalTax(baseForTaxes);
        
        response.setStateTax(stateTax);
        response.setFederalTax(federalTax);
        response.setTotal(request.getBaseAmount() - discount + stateTax + federalTax);
        
        return response;
    }

    private double calculateStateTax(String state, List<OrderItemDto> items, double base) {
        state = state.toUpperCase();
        switch (state) {
            case "RS":
                if (base <= 100.0) return 0.0;
                return (base - 100.0) * 0.10;
            case "SP":
                return base * 0.12;
            case "PE":
                double total = 0.0;
                for (OrderItemDto item : items) {
                    if (item.isEssential()) {
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

    private double calculateFederalTax(double base) {
        return base * 0.08;
    }

    private double calculateDiscount(List<OrderItemDto> items) {
        double totalQuantity = items.stream()
                .mapToInt(OrderItemDto::getQuantity)
                .sum();
        
        if (totalQuantity >= 10) {
            return 0.10;
        } else if (totalQuantity >= 5) {
            return 0.05;
        }
        return 0.0;
    }
} 