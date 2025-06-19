package com.eduardo.projarq.t1.servicodevendas.domain.repository;

import com.eduardo.projarq.t1.servicodevendas.domain.model.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(String id);
    List<Order> findAll();
    List<Order> findByStatus(Order.OrderStatus status);
    List<Order> findByPeriodAndStatus(LocalDateTime startDate, LocalDateTime endDate, Order.OrderStatus status);
    void delete(String id);
    boolean existsById(String id);
} 