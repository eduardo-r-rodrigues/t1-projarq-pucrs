package com.eduardo.projarq.t1.sales.repositories;

import com.eduardo.projarq.t1.sales.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByCode(String code);
    boolean existsByCode(String code);
} 