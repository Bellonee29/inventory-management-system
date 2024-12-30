package com.wizer.inventorymanagement.service;


import com.wizer.inventorymanagement.model.OrderReport;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    void processOrderMessage(String orderMessage);
    List<OrderReport> getOrderReports(LocalDate startDate, LocalDate endDate);
}
