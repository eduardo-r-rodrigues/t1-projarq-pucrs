package com.eduardo.projarq.t1.servicodevendas.application.dto;

import java.util.List;

public class OrderRequestDTO {
    public String customerId;
    public String state;
    public String country;
    public List<OrderItemRequestDTO> items;
} 