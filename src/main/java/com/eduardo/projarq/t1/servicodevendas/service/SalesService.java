package com.eduardo.projarq.t1.servicodevendas.service;

import com.eduardo.projarq.t1.servicodevendas.model.Order;
import com.eduardo.projarq.t1.servicodevendas.model.Product;

import java.util.List;

public interface SalesService {
    Order createOrder(Order order);
    Order getOrder(String id);
    List<Order> getAllOrders();
    Product createProduct(Product product);
    List<Product> getAllProducts();
    Product getProduct(String code);
} 