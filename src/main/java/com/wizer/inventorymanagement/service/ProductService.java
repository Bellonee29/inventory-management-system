package com.wizer.inventorymanagement.service;

import com.wizer.inventorymanagement.dto.ProductRequest;
import com.wizer.inventorymanagement.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(String id, ProductRequest request);

    Page<ProductResponse> getAllProducts(Pageable pageable);

    ProductResponse getProductByName(String name);

    void deleteProduct(String id);
}
