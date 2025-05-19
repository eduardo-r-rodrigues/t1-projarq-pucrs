package com.eduardo.projarq.t1.servicodevendas.domain.repository;

import com.eduardo.projarq.t1.servicodevendas.model.Product;
import java.util.*;

public class InMemoryProductRepository implements ProductRepository {
    private static final Map<String, Product> products = new HashMap<>();

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public Optional<Product> findByCode(String code) {
        return Optional.ofNullable(products.get(code));
    }

    @Override
    public void save(Product product) {
        products.put(product.getCode(), product);
    }

    @Override
    public void deleteByCode(String code) {
        products.remove(code);
    }
} 