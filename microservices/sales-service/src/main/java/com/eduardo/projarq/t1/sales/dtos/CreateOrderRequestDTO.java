package com.eduardo.projarq.t1.sales.dtos;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequestDTO {
    private String customerId;
    private String state;
    private String country;
    private List<OrderItemDTO> items;
} 