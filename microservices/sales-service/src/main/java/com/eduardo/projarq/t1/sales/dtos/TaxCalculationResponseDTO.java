package com.eduardo.projarq.t1.sales.dtos;

import lombok.Data;

@Data
public class TaxCalculationResponseDTO {
    private double discount;
    private double stateTax;
    private double federalTax;
    private double total;
} 