package com.eduardo.projarq.t1.servicodevendas.domain.service;

import com.eduardo.projarq.t1.servicodevendas.domain.model.OrderItem;
import com.eduardo.projarq.t1.servicodevendas.domain.model.Product;

import java.util.List;

public interface TaxCalculationService {
    double calculateStateTax(String state, List<OrderItem> items, double base);
    double calculateFederalTax(double base);
    double calculateDiscount(List<OrderItem> items);
} 