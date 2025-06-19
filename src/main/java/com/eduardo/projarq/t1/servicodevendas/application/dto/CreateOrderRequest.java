package com.eduardo.projarq.t1.servicodevendas.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private String customerId;
    private String state;
    private String country;
    private List<OrderItemRequest> items;
} 