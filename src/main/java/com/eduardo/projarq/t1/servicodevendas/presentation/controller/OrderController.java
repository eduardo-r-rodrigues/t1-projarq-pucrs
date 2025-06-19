package com.eduardo.projarq.t1.servicodevendas.presentation.controller;

import com.eduardo.projarq.t1.servicodevendas.application.dto.CreateOrderRequest;
import com.eduardo.projarq.t1.servicodevendas.application.service.OrderApplicationService;
import com.eduardo.projarq.t1.servicodevendas.domain.model.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final OrderApplicationService orderApplicationService;
    
    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }
    
    @GetMapping
    public ResponseEntity<List<Order>> getOrdersByPeriod(
            @RequestParam String start, 
            @RequestParam String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        List<Order> orders = orderApplicationService.getOrdersByPeriod(startDate, endDate);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable String id) {
        return orderApplicationService.getOrder(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderApplicationService.getAllOrders());
    }
    
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        Order order = orderApplicationService.createOrder(request);
        return ResponseEntity.ok(order);
    }
    
    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<Order> confirmOrder(@PathVariable String orderId) {
        Order order = orderApplicationService.confirmOrder(orderId);
        return ResponseEntity.ok(order);
    }
} 