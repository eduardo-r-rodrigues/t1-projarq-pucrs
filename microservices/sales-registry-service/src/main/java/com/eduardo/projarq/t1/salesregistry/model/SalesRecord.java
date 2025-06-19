package com.eduardo.projarq.t1.salesregistry.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sales_records")
@Data
@NoArgsConstructor
public class SalesRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_id", nullable = false)
    private String orderId;
    
    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;
    
    @Column(name = "total_amount", nullable = false)
    private double totalAmount;
    
    @Column(name = "tax_amount", nullable = false)
    private double taxAmount;
    
    @Column(name = "state", nullable = false)
    private String state;
    
    @Column(name = "customer_id", nullable = false)
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