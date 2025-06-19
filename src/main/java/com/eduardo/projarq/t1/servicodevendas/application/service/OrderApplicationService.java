package com.eduardo.projarq.t1.servicodevendas.application.service;

import com.eduardo.projarq.t1.servicodevendas.application.dto.CreateOrderRequest;
import com.eduardo.projarq.t1.servicodevendas.domain.model.Order;
import com.eduardo.projarq.t1.servicodevendas.domain.model.OrderItem;
import com.eduardo.projarq.t1.servicodevendas.domain.model.Product;
import com.eduardo.projarq.t1.servicodevendas.domain.repository.OrderRepository;
import com.eduardo.projarq.t1.servicodevendas.domain.repository.ProductRepository;
import com.eduardo.projarq.t1.servicodevendas.domain.service.TaxCalculationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderApplicationService {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final TaxCalculationService taxCalculationService;
    
    public OrderApplicationService(OrderRepository orderRepository, 
                                 ProductRepository productRepository,
                                 TaxCalculationService taxCalculationService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.taxCalculationService = taxCalculationService;
    }
    
    public Order createOrder(CreateOrderRequest request) {
        // Validate location
        if (!isSupportedCountry(request.getCountry()) || !isSupportedState(request.getState())) {
            throw new IllegalArgumentException("Country or state not supported");
        }
        
        // Create order items and validate stock
        List<OrderItem> items = new ArrayList<>();
        double subtotal = 0;
        
        for (var itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductCode())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + itemRequest.getProductCode()));
            
            if (!product.hasAvailableStock(itemRequest.getQuantity())) {
                throw new IllegalArgumentException("Insufficient stock for product: " + itemRequest.getProductCode());
            }
            
            OrderItem item = OrderItem.create(
                product.getCode(), 
                itemRequest.getQuantity(), 
                product.getUnitPrice()
            );
            items.add(item);
            subtotal += item.getTotalPrice();
        }
        
        // Calculate taxes and discount
        double discount = subtotal * taxCalculationService.calculateDiscount(items);
        double baseForTaxes = subtotal - discount;
        double stateTax = taxCalculationService.calculateStateTax(request.getState(), items, baseForTaxes);
        double federalTax = taxCalculationService.calculateFederalTax(baseForTaxes);
        double total = subtotal - discount + stateTax + federalTax;
        
        // Create order
        Order order = new Order(
            request.getCustomerId(),
            request.getState(),
            request.getCountry(),
            items,
            subtotal,
            discount,
            stateTax,
            federalTax,
            total
        );
        
        return orderRepository.save(order);
    }
    
    public Order confirmOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        // Validate order can be confirmed
        order.confirm();
        
        // Reserve stock for all items
        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProductCode())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProductCode()));
            
            product.reserveStock(item.getQuantity());
            productRepository.save(product);
        }
        
        return orderRepository.save(order);
    }
    
    public Optional<Order> getOrder(String id) {
        return orderRepository.findById(id);
    }
    
    public List<Order> getOrdersByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByPeriodAndStatus(startDate, endDate, Order.OrderStatus.CONFIRMED);
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    private boolean isSupportedCountry(String country) {
        return "Brasil".equalsIgnoreCase(country) || "Brazil".equalsIgnoreCase(country);
    }
    
    private boolean isSupportedState(String state) {
        return List.of("RS", "SP", "PE").contains(state.toUpperCase());
    }
} 