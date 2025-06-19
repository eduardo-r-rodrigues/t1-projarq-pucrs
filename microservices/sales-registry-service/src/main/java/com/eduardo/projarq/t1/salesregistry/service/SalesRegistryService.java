package com.eduardo.projarq.t1.salesregistry.service;

import com.eduardo.projarq.t1.salesregistry.dto.OrderApprovedMessage;
import com.eduardo.projarq.t1.salesregistry.model.SalesRecord;
import com.eduardo.projarq.t1.salesregistry.repository.SalesRecordRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SalesRegistryService {
    
    private final SalesRecordRepository salesRecordRepository;
    
    public SalesRegistryService(SalesRecordRepository salesRecordRepository) {
        this.salesRecordRepository = salesRecordRepository;
    }
    
    @RabbitListener(queues = "#{rabbitMQConfig.getQueueName()}")
    public void handleOrderApproved(OrderApprovedMessage message) {
        SalesRecord record = new SalesRecord(
            message.getOrderId(),
            message.getApprovedAt(),
            message.getTotalAmount(),
            message.getTaxAmount(),
            message.getState(),
            message.getCustomerId()
        );
        
        salesRecordRepository.save(record);
        System.out.println("Sales record saved for order: " + message.getOrderId());
    }
    
    public Map<String, Object> getMonthlyReport(int year, int month) {
        Double totalSales = salesRecordRepository.getTotalSalesByMonth(year, month);
        Double totalTaxes = salesRecordRepository.getTotalTaxesByMonth(year, month);
        
        Map<String, Object> report = new HashMap<>();
        report.put("year", year);
        report.put("month", month);
        report.put("totalSales", totalSales != null ? totalSales : 0.0);
        report.put("totalTaxes", totalTaxes != null ? totalTaxes : 0.0);
        
        return report;
    }
} 