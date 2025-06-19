package com.eduardo.projarq.t1.servicodevendas.infrastructure.client;

import com.eduardo.projarq.t1.servicodevendas.infrastructure.dto.TaxCalculationRequest;
import com.eduardo.projarq.t1.servicodevendas.infrastructure.dto.TaxCalculationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "tax-service")
public interface TaxServiceClient {
    
    @PostMapping("/api/tax/calculate")
    TaxCalculationResponse calculateTaxes(@RequestBody TaxCalculationRequest request);
} 