package com.eduardo.projarq.t1.servicodevendas.infrastructure.repository;

import com.eduardo.projarq.t1.servicodevendas.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SpringDataOrderRepository extends JpaRepository<Order, String> {
    
    List<Order> findByStatus(Order.OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate AND o.status = :status")
    List<Order> findByPeriodAndStatus(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate, 
                                     @Param("status") Order.OrderStatus status);
} 