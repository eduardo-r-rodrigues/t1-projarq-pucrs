package com.eduardo.projarq.t1.servicodevendas.infrastructure.repository;

import com.eduardo.projarq.t1.servicodevendas.domain.model.Order;
import com.eduardo.projarq.t1.servicodevendas.domain.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaOrderRepository implements OrderRepository {
    
    private final SpringDataOrderRepository springDataOrderRepository;
    
    public JpaOrderRepository(SpringDataOrderRepository springDataOrderRepository) {
        this.springDataOrderRepository = springDataOrderRepository;
    }
    
    @Override
    public Order save(Order order) {
        return springDataOrderRepository.save(order);
    }
    
    @Override
    public Optional<Order> findById(String id) {
        return springDataOrderRepository.findById(id);
    }
    
    @Override
    public List<Order> findAll() {
        return springDataOrderRepository.findAll();
    }
    
    @Override
    public List<Order> findByStatus(Order.OrderStatus status) {
        return springDataOrderRepository.findByStatus(status);
    }
    
    @Override
    public List<Order> findByPeriodAndStatus(LocalDateTime startDate, LocalDateTime endDate, Order.OrderStatus status) {
        return springDataOrderRepository.findByPeriodAndStatus(startDate, endDate, status);
    }
    
    @Override
    public void delete(String id) {
        springDataOrderRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(String id) {
        return springDataOrderRepository.existsById(id);
    }
} 