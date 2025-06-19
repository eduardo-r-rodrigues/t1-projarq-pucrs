package com.eduardo.projarq.t1.servicodevendas.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
public class Order {
    @Id
    private String id;
    
    @Column(name = "customer_id", nullable = false)
    private String customerId;
    
    @Column(nullable = false)
    private String state;
    
    @Column(nullable = false)
    private String country;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    @Embedded
    private List<OrderItem> items = new ArrayList<>();
    
    @Column(nullable = false)
    private double subtotal;
    
    @Column(nullable = false)
    private double discount;
    
    @Column(name = "state_tax", nullable = false)
    private double stateTax;
    
    @Column(name = "federal_tax", nullable = false)
    private double federalTax;
    
    @Column(nullable = false)
    private double total;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    public Order(String customerId, String state, String country, List<OrderItem> items,
                double subtotal, double discount, double stateTax, double federalTax, double total) {
        this.id = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.state = state;
        this.country = country;
        this.items = new ArrayList<>(items);
        this.subtotal = subtotal;
        this.discount = discount;
        this.stateTax = stateTax;
        this.federalTax = federalTax;
        this.total = total;
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusDays(21);
    }

    // Domain methods
    public boolean isPending() {
        return status == OrderStatus.PENDING;
    }

    public boolean isConfirmed() {
        return status == OrderStatus.CONFIRMED;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public void confirm() {
        if (!isPending()) {
            throw new IllegalStateException("Order is not pending");
        }
        if (isExpired()) {
            this.status = OrderStatus.EXPIRED;
            throw new IllegalStateException("Order expired");
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public void expire() {
        this.status = OrderStatus.EXPIRED;
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public enum OrderStatus {
        PENDING, CONFIRMED, EXPIRED, CANCELLED
    }
} 