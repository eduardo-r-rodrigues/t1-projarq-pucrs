package com.eduardo.projarq.t1.servicodevendas.controller;

import com.eduardo.projarq.t1.servicodevendas.adapters.repository.InMemoryOrderRepository;
import com.eduardo.projarq.t1.servicodevendas.adapters.repository.InMemoryProductRepository;
import com.eduardo.projarq.t1.servicodevendas.model.*;
import com.eduardo.projarq.t1.servicodevendas.application.dto.OrderRequestDTO;
import com.eduardo.projarq.t1.servicodevendas.application.dto.OrderItemRequestDTO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sales")
public class SalesController {
    private final InMemoryProductRepository productRepo = new InMemoryProductRepository();
    private final InMemoryOrderRepository orderRepo = new InMemoryOrderRepository();

    @GetMapping("/products")
    public List<Product> getProducts() {
        return productRepo.findAll();
    }

    @PostMapping("/orders/quote")
    public Order quoteOrder(@RequestBody OrderRequestDTO request) {
        if (!isSupportedCountry(request.country) || !isSupportedState(request.state)) {
            throw new IllegalArgumentException("Country or state not supported");
        }
        List<OrderItem> items = new ArrayList<>();
        double subtotal = 0;
        for (OrderItemRequestDTO itemReq : request.items) {
            Product product = productRepo.findByCode(itemReq.productCode)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + itemReq.productCode));
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
        orderRepo.save(order);
        return order;
    }

    @PostMapping("/orders/{orderId}/confirm")
    public Order confirmOrder(@PathVariable String orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (!order.getStatus().equals("PENDING")) {
            throw new IllegalStateException("Order is not pending");
        }
        if (order.getExpiresAt().isBefore(LocalDateTime.now())) {
            order.setStatus("EXPIRED");
            orderRepo.save(order);
            throw new IllegalStateException("Order expired");
        }
        for (OrderItem item : order.getItems()) {
            Product product = productRepo.findByCode(item.getProductCode())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProductCode()));
            if (item.getQuantity() > product.getAvailableQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + item.getProductCode());
            }
        }
        for (OrderItem item : order.getItems()) {
            Product product = productRepo.findByCode(item.getProductCode()).get();
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            product.setAvailableQuantity(product.getAvailableQuantity() - item.getQuantity());
            productRepo.save(product);
        }
        order.setStatus("CONFIRMED");
        orderRepo.save(order);
        return order;
    }

    @PostMapping("/products/{code}/restock")
    public Product restockProduct(@PathVariable String code, @RequestParam int quantity) {
        Product product = productRepo.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        int newStock = Math.min(product.getStockQuantity() + quantity, product.getMaxStockQuantity());
        product.setAvailableQuantity(product.getAvailableQuantity() + (newStock - product.getStockQuantity()));
        product.setStockQuantity(newStock);
        productRepo.save(product);
        return product;
    }

    @GetMapping("/products/stock")
    public Map<String, Integer> getStockForAllProducts() {
        Map<String, Integer> stock = new HashMap<>();
        for (Product p : productRepo.findAll()) {
            stock.put(p.getCode(), p.getAvailableQuantity());
        }
        return stock;
    }

    @PostMapping("/products/stock")
    public Map<String, Integer> getStockForProducts(@RequestBody List<String> codes) {
        Map<String, Integer> stock = new HashMap<>();
        for (String code : codes) {
            productRepo.findByCode(code).ifPresent(p -> stock.put(code, p.getAvailableQuantity()));
        }
        return stock;
    }

    @GetMapping("/orders")
    public List<Order> getOrdersByPeriod(@RequestParam String start, @RequestParam String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return orderRepo.findByPeriod(startDate, endDate).stream()
                .filter(o -> "CONFIRMED".equals(o.getStatus()))
                .collect(Collectors.toList());
    }

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
                    Product product = productRepo.findByCode(item.getProductCode()).orElse(null);
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
