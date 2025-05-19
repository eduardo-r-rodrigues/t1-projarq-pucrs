package com.eduardo.projarq.t1.servicodevendas.service;

import com.eduardo.projarq.t1.servicodevendas.model.Order;
import com.eduardo.projarq.t1.servicodevendas.model.Product;
import com.eduardo.projarq.t1.servicodevendas.repository.InMemoryOrderRepository;
import com.eduardo.projarq.t1.servicodevendas.repository.InMemoryProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesServiceImpl implements SalesService {

    private final InMemoryOrderRepository orderRepository;
    private final InMemoryProductRepository productRepository;

    public SalesServiceImpl(InMemoryOrderRepository orderRepository, InMemoryProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order getOrder(String id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(String code) {
        return productRepository.findById(code);
    }
} 