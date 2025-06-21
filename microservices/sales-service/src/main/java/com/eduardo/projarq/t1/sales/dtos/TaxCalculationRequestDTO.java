package com.eduardo.projarq.t1.sales.dtos;

import java.util.List;

public class TaxCalculationRequestDTO {
    private String state;
    private List<OrderItemDTO> items;
    private double baseAmount;

    // Getters and Setters
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
    public double getBaseAmount() { return baseAmount; }
    public void setBaseAmount(double baseAmount) { this.baseAmount = baseAmount; }
} 