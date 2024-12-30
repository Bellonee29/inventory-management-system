package com.wizer.inventorymanagement.service.serviceImpl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wizer.inventorymanagement.model.OrderReport;
import com.wizer.inventorymanagement.repository.OrderReportRepository;
import com.wizer.inventorymanagement.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final OrderReportRepository orderReportRepository;
    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    public ReportServiceImpl(OrderReportRepository orderReportRepository) {
        this.orderReportRepository = orderReportRepository;
    }

    /**
     * Processes an order message received from Kafka by updating or creating an order report.
     *
     * @param orderMessage the order message in JSON format containing order details
     */
    @Override
    @KafkaListener(topics = "order_topic", groupId = "report_group")
    public void processOrderMessage(String orderMessage) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        try {
            JsonNode jsonNode = objectMapper.readTree(orderMessage);
            String orderDateString = jsonNode.get("orderDate").asText();
            logger.info("Json data:{}", jsonNode);

            LocalDate orderDate;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                orderDate = LocalDate.parse(orderDateString, formatter);
            } catch (DateTimeParseException e) {
                System.err.println("Invalid orderDate format: " + orderDateString);
                return;
            }
            BigDecimal orderAmount = jsonNode.get("totalPrice").decimalValue();

            OrderReport orderReport = orderReportRepository.findByOrderDate(orderDate)
                    .orElse(new OrderReport(orderDate, 0, BigDecimal.ZERO));
            orderReport.setTotalOrders(orderReport.getTotalOrders() + 1);
            orderReport.setTotalOrderAmount(orderReport.getTotalOrderAmount().add(orderAmount));

            orderReportRepository.save(orderReport);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a list of order reports within a specified date range.
     *
     * @param startDate the start date for the date range filter
     * @param endDate the end date for the date range filter
     * @return List<OrderReport> the list of order reports within the specified date range
     */
    @Override
    public List<OrderReport> getOrderReports(LocalDate startDate, LocalDate endDate) {
        return orderReportRepository.findByOrderDateBetween(startDate, endDate);
    }
}
