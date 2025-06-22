package com.eduardo.projarq.t1.salesregistry.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sales_records")
@Data
@NoArgsConstructor
public class SalesRecord {
    @Id
    private String id;
    
    private String orderId;
    
    private LocalDateTime saleDate;
    
    private double totalAmount;
    
    private double taxAmount;
    
    private String state;
    
    private String customerId;
    
    public SalesRecord(String orderId, LocalDateTime saleDate, double totalAmount, double taxAmount, String state, String customerId) {
        this.orderId = orderId;
        this.saleDate = saleDate;
        this.totalAmount = totalAmount;
        this.taxAmount = taxAmount;
        this.state = state;
        this.customerId = customerId;
    }
} 