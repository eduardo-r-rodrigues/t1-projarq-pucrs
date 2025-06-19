package com.eduardo.projarq.t1.servicodevendas.infrastructure.repository;

import com.eduardo.projarq.t1.servicodevendas.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataProductRepository extends JpaRepository<Product, String> {
} 