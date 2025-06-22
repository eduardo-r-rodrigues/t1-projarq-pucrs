package com.eduardo.projarq.t1.salesregistry.repository;

import com.eduardo.projarq.t1.salesregistry.model.SalesRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalesRecordRepository extends MongoRepository<SalesRecord, String> {
    List<SalesRecord> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);
} 