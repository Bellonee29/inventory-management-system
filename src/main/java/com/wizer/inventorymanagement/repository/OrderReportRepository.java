package com.wizer.inventorymanagement.repository;

import com.wizer.inventorymanagement.model.OrderReport;
import java.util.Optional;import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface OrderReportRepository extends JpaRepository<OrderReport, Long> {
    List<OrderReport> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    Optional<OrderReport> findByOrderDate(LocalDate orderDate);
}


