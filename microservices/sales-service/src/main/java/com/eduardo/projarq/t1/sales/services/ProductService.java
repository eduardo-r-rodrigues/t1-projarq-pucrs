package com.eduardo.projarq.t1.sales.services;

import com.eduardo.projarq.t1.sales.dtos.ProductDTO;
import com.eduardo.projarq.t1.sales.models.Product;
import com.eduardo.projarq.t1.sales.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<ProductDTO> getProductByCode(String code) {
        return productRepository.findByCode(code)
                .map(this::convertToDTO);
    }
    
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setCode(productDTO.getCode());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setEssential(productDTO.isEssential());
        
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }
    
    public ProductDTO restockProduct(String code, int quantity) {
        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Product not found: " + code));
        
        product.setStock(product.getStock() + quantity);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }
    
    public void reserveStock(String code, int quantity) {
        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Product not found: " + code));
        
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + code);
        }
        
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }
    
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setCode(product.getCode());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setEssential(product.isEssential());
        return dto;
    }
} 