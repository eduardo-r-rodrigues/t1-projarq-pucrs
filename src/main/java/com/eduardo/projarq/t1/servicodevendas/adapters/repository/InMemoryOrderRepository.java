package com.eduardo.projarq.t1.servicodevendas.domain.repository;

import com.eduardo.projarq.t1.servicodevendas.model.Order;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryOrderRepository implements OrderRepository {
    private static final Map<String, Order> orders = new HashMap<>();

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public Optional<Order> findById(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public void save(Order order) {
        orders.put(order.getId(), order);
    }

    @Override
    public void deleteById(String id) {
        orders.remove(id);
    }

    @Override
    public List<Order> findByPeriod(LocalDateTime start, LocalDateTime end) {
        return orders.values().stream()
                .filter(o -> o.getCreatedAt() != null && !o.getCreatedAt().isBefore(start) && !o.getCreatedAt().isAfter(end))
                .collect(Collectors.toList());
    }
} 