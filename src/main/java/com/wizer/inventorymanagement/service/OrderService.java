package com.wizer.inventorymanagement.service;

import com.wizer.inventorymanagement.dto.OrderRequest;
import com.wizer.inventorymanagement.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponse placeOrder (OrderRequest request);

    Page<OrderResponse> getAllOrders(Pageable pageable);
}
