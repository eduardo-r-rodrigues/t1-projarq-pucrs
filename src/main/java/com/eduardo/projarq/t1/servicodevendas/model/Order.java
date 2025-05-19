package com.eduardo.projarq.t1.servicodevendas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String id;
    private String customerId;
    private String state;
    private String country;
    private List<OrderItem> items;
    private double subtotal;
    private double discount;
    private double stateTax;
    private double federalTax;
    private double total;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
