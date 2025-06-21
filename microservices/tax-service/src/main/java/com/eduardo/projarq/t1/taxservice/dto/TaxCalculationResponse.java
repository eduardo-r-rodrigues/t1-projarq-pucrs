package com.eduardo.projarq.t1.taxservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaxCalculationResponse {
    private double discount;
    private double stateTax;
    private double federalTax;
    private double total;
} 