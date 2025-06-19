package com.eduardo.projarq.t1.salesregistry.repository;

import com.eduardo.projarq.t1.salesregistry.model.SalesRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalesRecordRepository extends JpaRepository<SalesRecord, Long> {
    
    @Query("SELECT SUM(s.totalAmount) FROM SalesRecord s WHERE YEAR(s.saleDate) = :year AND MONTH(s.saleDate) = :month")
    Double getTotalSalesByMonth(@Param("year") int year, @Param("month") int month);
    
    @Query("SELECT SUM(s.taxAmount) FROM SalesRecord s WHERE YEAR(s.saleDate) = :year AND MONTH(s.saleDate) = :month")
    Double getTotalTaxesByMonth(@Param("year") int year, @Param("month") int month);
    
    List<SalesRecord> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);
} 