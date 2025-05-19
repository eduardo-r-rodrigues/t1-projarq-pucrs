package com.eduardo.projarq.t1.servicodevendas.controller;

import com.eduardo.projarq.t1.servicodevendas.model.*;
import com.eduardo.projarq.t1.servicodevendas.application.dto.OrderRequestDTO;
import com.eduardo.projarq.t1.servicodevendas.application.dto.OrderItemRequestDTO;
import com.eduardo.projarq.t1.servicodevendas.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    @Autowired
    private SalesService salesService;

    // Product endpoints
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(salesService.getAllProducts());
    }

    @GetMapping("/products/{code}")
    public ResponseEntity<Product> getProduct(@PathVariable String code) {
        return ResponseEntity.ok(salesService.getProduct(code));
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(salesService.createProduct(product));
    }

    @PostMapping("/products/{code}/restock")
    public ResponseEntity<Product> restockProduct(@PathVariable String code, @RequestParam int quantity) {
        Product product = salesService.getProduct(code);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }
        int newStock = Math.min(product.getStockQuantity() + quantity, product.getMaxStockQuantity());
        product.setAvailableQuantity(product.getAvailableQuantity() + (newStock - product.getStockQuantity()));
        product.setStockQuantity(newStock);
        return ResponseEntity.ok(salesService.createProduct(product));
    }

    @GetMapping("/products/stock")
    public ResponseEntity<Map<String, Integer>> getStockForAllProducts() {
        Map<String, Integer> stock = new HashMap<>();
        for (Product p : salesService.getAllProducts()) {
            stock.put(p.getCode(), p.getAvailableQuantity());
        }
        return ResponseEntity.ok(stock);
    }

    @PostMapping("/products/stock")
    public ResponseEntity<Map<String, Integer>> getStockForProducts(@RequestBody List<String> codes) {
        Map<String, Integer> stock = new HashMap<>();
        for (String code : codes) {
            Product product = salesService.getProduct(code);
            if (product != null) {
                stock.put(code, product.getAvailableQuantity());
            }
        }
        return ResponseEntity.ok(stock);
    }

    // Order endpoints
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrdersByPeriod(@RequestParam String start, @RequestParam String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        List<Order> allOrders = salesService.getAllOrders();
        List<Order> filteredOrders = allOrders.stream()
                .filter(o -> o.getCreatedAt().isAfter(startDate) && o.getCreatedAt().isBefore(endDate))
                .filter(o -> "CONFIRMED".equals(o.getStatus()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(filteredOrders);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable String id) {
        return ResponseEntity.ok(salesService.getOrder(id));
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(salesService.createOrder(order));
    }

    @PostMapping("/orders/quote")
    public ResponseEntity<Order> quoteOrder(@RequestBody OrderRequestDTO request) {
        if (!isSupportedCountry(request.country) || !isSupportedState(request.state)) {
            throw new IllegalArgumentException("Country or state not supported");
        }
        List<OrderItem> items = new ArrayList<>();
        double subtotal = 0;
        for (OrderItemRequestDTO itemReq : request.items) {
            Product product = salesService.getProduct(itemReq.productCode);
            if (product == null) {
                throw new IllegalArgumentException("Product not found: " + itemReq.productCode);
            }
            if (itemReq.quantity > product.getAvailableQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + itemReq.productCode);
            }
            double totalPrice = product.getUnitPrice() * itemReq.quantity;
            items.add(new OrderItem(product.getCode(), itemReq.quantity, product.getUnitPrice(), totalPrice));
            subtotal += totalPrice;
        }
        double discount = calculateDiscount(items);
        double stateTax = calculateStateTax(request.state, items, subtotal - discount);
        double federalTax = calculateFederalTax(subtotal - discount);
        double total = subtotal - discount + stateTax + federalTax;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusDays(21);
        Order order = new Order(UUID.randomUUID().toString(), request.customerId, request.state, request.country, items,
                subtotal, discount, stateTax, federalTax, total, "PENDING", now, expiresAt);
        return ResponseEntity.ok(salesService.createOrder(order));
    }

    @PostMapping("/orders/{orderId}/confirm")
    public ResponseEntity<Order> confirmOrder(@PathVariable String orderId) {
        Order order = salesService.getOrder(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }
        if (!order.getStatus().equals("PENDING")) {
            throw new IllegalStateException("Order is not pending");
        }
        if (order.getExpiresAt().isBefore(LocalDateTime.now())) {
            order.setStatus("EXPIRED");
            salesService.createOrder(order);
            throw new IllegalStateException("Order expired");
        }
        for (OrderItem item : order.getItems()) {
            Product product = salesService.getProduct(item.getProductCode());
            if (product == null) {
                throw new IllegalArgumentException("Product not found: " + item.getProductCode());
            }
            if (item.getQuantity() > product.getAvailableQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + item.getProductCode());
            }
        }
        for (OrderItem item : order.getItems()) {
            Product product = salesService.getProduct(item.getProductCode());
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            product.setAvailableQuantity(product.getAvailableQuantity() - item.getQuantity());
            salesService.createProduct(product);
        }
        order.setStatus("CONFIRMED");
        return ResponseEntity.ok(salesService.createOrder(order));
    }

    // Helper methods
    private boolean isSupportedCountry(String country) {
        return "Brasil".equalsIgnoreCase(country) || "Brazil".equalsIgnoreCase(country);
    }

    private boolean isSupportedState(String state) {
        return Arrays.asList("RS", "SP", "PE").contains(state.toUpperCase());
    }

    private double calculateDiscount(List<OrderItem> items) {
        int totalItems = items.stream().mapToInt(OrderItem::getQuantity).sum();
        double subtotal = items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
        if (totalItems > 10) {
            return subtotal * 0.10;
        } else if (totalItems > 3) {
            return subtotal * 0.05;
        }
        return 0.0;
    }

    private double calculateStateTax(String state, List<OrderItem> items, double base) {
        state = state.toUpperCase();
        switch (state) {
            case "RS":
                if (base <= 100.0) return 0.0;
                return (base - 100.0) * 0.10;
            case "SP":
                return base * 0.12;
            case "PE":
                double total = 0.0;
                for (OrderItem item : items) {
                    Product product = salesService.getProduct(item.getProductCode());
                    if (product != null && product.isEssential()) {
                        total += item.getTotalPrice() * 0.05;
                    } else {
                        total += item.getTotalPrice() * 0.15;
                    }
                }
                return total;
            default:
                return 0.0;
        }
    }

    private double calculateFederalTax(double base) {
        return base * 0.15;
    }
}
