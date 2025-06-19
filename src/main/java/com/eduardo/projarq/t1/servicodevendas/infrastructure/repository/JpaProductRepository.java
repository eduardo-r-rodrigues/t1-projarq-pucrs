package com.eduardo.projarq.t1.servicodevendas.infrastructure.repository;

import com.eduardo.projarq.t1.servicodevendas.domain.model.Product;
import com.eduardo.projarq.t1.servicodevendas.domain.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaProductRepository implements ProductRepository {
    
    private final SpringDataProductRepository springDataProductRepository;
    
    public JpaProductRepository(SpringDataProductRepository springDataProductRepository) {
        this.springDataProductRepository = springDataProductRepository;
    }
    
    @Override
    public Product save(Product product) {
        return springDataProductRepository.save(product);
    }
    
    @Override
    public Optional<Product> findById(String code) {
        return springDataProductRepository.findById(code);
    }
    
    @Override
    public List<Product> findAll() {
        return springDataProductRepository.findAll();
    }
    
    @Override
    public void delete(String code) {
        springDataProductRepository.deleteById(code);
    }
    
    @Override
    public boolean existsById(String code) {
        return springDataProductRepository.existsById(code);
    }
} 