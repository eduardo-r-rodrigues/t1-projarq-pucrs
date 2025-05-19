package com.eduardo.projarq.t1.servicodevendas.domain.repository;

import com.eduardo.projarq.t1.servicodevendas.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    List<Order> findAll();
    Optional<Order> findById(String id);
    void save(Order order);
    void deleteById(String id);
    List<Order> findByPeriod(java.time.LocalDateTime start, java.time.LocalDateTime end);
} 