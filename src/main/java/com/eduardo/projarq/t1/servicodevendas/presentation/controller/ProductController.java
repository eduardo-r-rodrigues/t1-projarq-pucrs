package com.eduardo.projarq.t1.servicodevendas.presentation.controller;

import com.eduardo.projarq.t1.servicodevendas.application.dto.CreateProductRequest;
import com.eduardo.projarq.t1.servicodevendas.application.service.ProductApplicationService;
import com.eduardo.projarq.t1.servicodevendas.domain.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    private final ProductApplicationService productApplicationService;
    
    public ProductController(ProductApplicationService productApplicationService) {
        this.productApplicationService = productApplicationService;
    }
    
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productApplicationService.getAllProducts());
    }
    
    @GetMapping("/{code}")
    public ResponseEntity<Product> getProduct(@PathVariable String code) {
        return productApplicationService.getProduct(code)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request) {
        Product product = productApplicationService.createProduct(request);
        return ResponseEntity.ok(product);
    }
    
    @PostMapping("/{code}/restock")
    public ResponseEntity<Product> restockProduct(@PathVariable String code, @RequestParam int quantity) {
        Product product = productApplicationService.restockProduct(code, quantity);
        return ResponseEntity.ok(product);
    }
    
    @GetMapping("/stock")
    public ResponseEntity<Map<String, Integer>> getStockForAllProducts() {
        Map<String, Integer> stock = productApplicationService.getAllProducts().stream()
            .collect(Collectors.toMap(Product::getCode, Product::getAvailableQuantity));
        return ResponseEntity.ok(stock);
    }
    
    @PostMapping("/stock")
    public ResponseEntity<Map<String, Integer>> getStockForProducts(@RequestBody List<String> codes) {
        Map<String, Integer> stock = codes.stream()
            .collect(Collectors.toMap(
                code -> code,
                code -> productApplicationService.getProduct(code)
                    .map(Product::getAvailableQuantity)
                    .orElse(0)
            ));
        return ResponseEntity.ok(stock);
    }
    
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String code) {
        productApplicationService.deleteProduct(code);
        return ResponseEntity.noContent().build();
    }
} 