package com.eduardo.projarq.t1.servicodevendas.application.service;

import com.eduardo.projarq.t1.servicodevendas.application.dto.CreateProductRequest;
import com.eduardo.projarq.t1.servicodevendas.domain.model.Product;
import com.eduardo.projarq.t1.servicodevendas.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductApplicationService {
    
    private final ProductRepository productRepository;
    
    public ProductApplicationService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public Product createProduct(CreateProductRequest request) {
        Product product = new Product(
            request.getCode(),
            request.getDescription(),
            request.getUnitPrice(),
            request.getMaxStockQuantity(),
            request.isEssential()
        );
        return productRepository.save(product);
    }
    
    public Optional<Product> getProduct(String code) {
        return productRepository.findById(code);
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public Product restockProduct(String code, int quantity) {
        Product product = productRepository.findById(code)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        
        product.addStock(quantity);
        return productRepository.save(product);
    }
    
    public void deleteProduct(String code) {
        productRepository.delete(code);
    }
} 