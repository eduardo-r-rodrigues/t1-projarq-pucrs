package com.eduardo.projarq.t1.taxservice.controller;

import com.eduardo.projarq.t1.taxservice.dto.TaxCalculationRequest;
import com.eduardo.projarq.t1.taxservice.dto.TaxCalculationResponse;
import com.eduardo.projarq.t1.taxservice.service.TaxCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tax")
public class TaxController {
    
    private final TaxCalculationService taxCalculationService;
    
    public TaxController(TaxCalculationService taxCalculationService) {
        this.taxCalculationService = taxCalculationService;
    }
    
    @PostMapping("/calculate")
    public ResponseEntity<TaxCalculationResponse> calculateTaxes(@RequestBody TaxCalculationRequest request) {
        TaxCalculationResponse response = taxCalculationService.calculateTaxes(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Tax Service is running!");
    }
} 