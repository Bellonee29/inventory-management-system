package com.wizer.inventorymanagement.service.serviceImpl;

import com.wizer.inventorymanagement.dto.ProductRequest;
import com.wizer.inventorymanagement.dto.ProductResponse;
import com.wizer.inventorymanagement.model.Product;
import com.wizer.inventorymanagement.repository.ProductRepository;
import com.wizer.inventorymanagement.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Creates a new product.
     *
     * @param request the product request containing product details
     * @return ProductResponse the response containing created product details
     * @throws IllegalArgumentException if a product with the same name already exists
     */
    @Override
    public ProductResponse createProduct(ProductRequest request) {

        Optional<Product> existingProduct = productRepository.findByProductName(request.getProductName());
        if (existingProduct.isPresent()) {
            throw new IllegalArgumentException("Product with the same name already exists.");
        }
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setProductDescription(request.getProductDescription());
        product.setProductPrice(request.getProductPrice());
        product.setStock(request.getStock());

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    /**
     * Updates an existing product.
     *
     * @param id the product ID
     * @param request the product request containing updated product details
     * @return ProductResponse the response containing updated product details
     * @throws RuntimeException if the product is not found
     */
    @Override
    public ProductResponse updateProduct(String id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Update only the supplied fields
        if (request.getProductName() != null) {
            product.setProductName(request.getProductName());
        }
        if (request.getProductDescription() != null) {
            product.setProductDescription(request.getProductDescription());
        }
        if (request.getProductPrice() != null) {
            product.setProductPrice(request.getProductPrice());
        }
        if (request.getStock() != null) {
            product.setStock(request.getStock());
        }

        Product updatedProduct = productRepository.save(product);

        return mapToResponse(updatedProduct);
    }


    /**
     * Retrieves a paginated list of all products.
     *
     * @param pageable the pagination information
     * @return Page<ProductResponse> the paginated list of products
     */
    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::mapToResponse);
    }

    /**
     * Retrieves a product by its name.
     *
     * @param name the product name
     * @return ProductResponse the response containing product details
     * @throws RuntimeException if the product is not found
     */
    @Override
    public ProductResponse getProductByName(String name) {
        Product product = productRepository.findByProductName(name)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return mapToResponse(product);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the product ID
     * @throws RuntimeException if the product is not found
     */
    @Override
    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    /**
     * Maps a Product entity to a ProductResponse.
     *
     * @param product the product entity
     * @return ProductResponse the response containing product details
     */
    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setProductName(product.getProductName());
        response.setProductDescription(product.getProductDescription());
        response.setProductPrice(product.getProductPrice());
        response.setStock(product.getStock());
        return response;
    }
}
