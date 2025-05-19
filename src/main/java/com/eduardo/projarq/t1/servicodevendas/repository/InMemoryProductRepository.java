package com.eduardo.projarq.t1.servicodevendas.repository;

import com.eduardo.projarq.t1.servicodevendas.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryProductRepository {
    private final Map<String, Product> products = new ConcurrentHashMap<>();

    public Product save(Product product) {
        products.put(product.getCode(), product);
        return product;
    }

    public Product findById(String code) {
        return products.get(code);
    }

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public void delete(String code) {
        products.remove(code);
    }
} 