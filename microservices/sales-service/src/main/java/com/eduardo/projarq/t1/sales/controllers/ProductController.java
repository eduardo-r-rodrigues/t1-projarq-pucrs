package com.eduardo.projarq.t1.sales.controllers;

import com.eduardo.projarq.t1.sales.dtos.ProductDTO;
import com.eduardo.projarq.t1.sales.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/{code}")
    public ResponseEntity<ProductDTO> getProductByCode(@PathVariable String code) {
        Optional<ProductDTO> product = productService.getProductByCode(code);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(createdProduct);
    }
    
    @PostMapping("/{code}/restock")
    public ResponseEntity<ProductDTO> restockProduct(
            @PathVariable String code,
            @RequestParam int quantity) {
        ProductDTO updatedProduct = productService.restockProduct(code, quantity);
        return ResponseEntity.ok(updatedProduct);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Sales Service is running!");
    }
} 