package com.eduardo.projarq.t1.servicodevendas.domain.repository;

import com.eduardo.projarq.t1.servicodevendas.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(String code);
    List<Product> findAll();
    void delete(String code);
    boolean existsById(String code);
}
