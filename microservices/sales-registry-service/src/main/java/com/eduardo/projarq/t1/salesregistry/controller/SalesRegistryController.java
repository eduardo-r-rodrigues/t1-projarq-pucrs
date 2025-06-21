package com.eduardo.projarq.t1.salesregistry.controller;

import com.eduardo.projarq.t1.salesregistry.service.SalesRegistryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sales-registry")
public class SalesRegistryController {
    
    private final SalesRegistryService salesRegistryService;
    
    public SalesRegistryController(SalesRegistryService salesRegistryService) {
        this.salesRegistryService = salesRegistryService;
    }
    
    @GetMapping("/report/{year}/{month}")
    public ResponseEntity<Map<String, Object>> getMonthlyReport(
            @PathVariable int year, 
            @PathVariable int month) {
        Map<String, Object> report = salesRegistryService.getMonthlyReport(year, month);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Sales Registry Service is running!");
    }
} 