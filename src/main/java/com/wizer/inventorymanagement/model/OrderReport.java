package com.wizer.inventorymanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "order_reports")
@NoArgsConstructor
public class OrderReport extends BaseEntity{

    private LocalDate orderDate;
    private Integer totalOrders;
    private BigDecimal totalOrderAmount;

    public OrderReport(LocalDate orderDate, Integer totalOrders, BigDecimal totalOrderAmount) {
        this.orderDate = orderDate;
        this.totalOrders = totalOrders;
        this.totalOrderAmount = totalOrderAmount;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(BigDecimal totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }
}

