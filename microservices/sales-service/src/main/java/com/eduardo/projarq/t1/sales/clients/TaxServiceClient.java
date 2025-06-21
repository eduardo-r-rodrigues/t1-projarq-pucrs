package com.eduardo.projarq.t1.sales.clients;

import com.eduardo.projarq.t1.sales.dtos.TaxCalculationRequestDTO;
import com.eduardo.projarq.t1.sales.dtos.TaxCalculationResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "tax-service")
public interface TaxServiceClient {
    
    @PostMapping("/api/tax/calculate")
    TaxCalculationResponseDTO calculateTaxes(@RequestBody TaxCalculationRequestDTO request);
} 