package com.wizer.inventorymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;




public class OrderResponse {
    private String id;
    private String customerName;
    private String customerPhone;
    private String productNames;
    private BigDecimal totalPrice;
    private LocalDate orderDate;

    public OrderResponse(String id, String customerName, String customerPhone, String productNames, BigDecimal totalPrice, LocalDate orderDate) {
        this.id = id;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.productNames = productNames;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    public OrderResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getProductNames() {
        return productNames;
    }

    public void setProductNames(String productNames) {
        this.productNames = productNames;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
}
