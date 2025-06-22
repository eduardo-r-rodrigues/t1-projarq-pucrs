package com.eduardo.projarq.t1.salesregistry.service;

import com.eduardo.projarq.t1.salesregistry.dto.OrderApprovedMessage;
import com.eduardo.projarq.t1.salesregistry.model.SalesRecord;
import com.eduardo.projarq.t1.salesregistry.repository.SalesRecordRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class SalesRegistryService {
    
    private final SalesRecordRepository salesRecordRepository;
    private final MongoTemplate mongoTemplate;
    
    public SalesRegistryService(SalesRecordRepository salesRecordRepository, MongoTemplate mongoTemplate) {
        this.salesRecordRepository = salesRecordRepository;
        this.mongoTemplate = mongoTemplate;
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
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1);

        MatchOperation matchOperation = Aggregation.match(new Criteria("saleDate").gte(startDate).lt(endDate));
        GroupOperation groupOperation = Aggregation.group().sum("totalAmount").as("totalSales")
                                                      .sum("taxAmount").as("totalTaxes");

        Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation);
        AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, "sales_records", Map.class);
        
        Map<String, Object> aggregatedResults = result.getUniqueMappedResult();
        
        Map<String, Object> report = new HashMap<>();
        report.put("year", year);
        report.put("month", month);
        report.put("totalSales", aggregatedResults != null ? aggregatedResults.get("totalSales") : 0.0);
        report.put("totalTaxes", aggregatedResults != null ? aggregatedResults.get("totalTaxes") : 0.0);
        
        return report;
    }
} 