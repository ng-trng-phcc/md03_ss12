package com.example.medicalsuppliesmanagement.model.repository;

import com.example.medicalsuppliesmanagement.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t.supplyId, SUM(t.quantity) FROM Transaction t WHERE t.type = 'EXPORT' AND t.createdAt BETWEEN :start AND :end GROUP BY t.supplyId")
    List<Object[]> getDailyExportStatistics(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT t.supplyId, SUM(t.quantity) as total FROM Transaction t WHERE t.type = 'EXPORT' GROUP BY t.supplyId ORDER BY total DESC")
    List<Object[]> findTopExportSupply();

    long countByType(String type);
}
