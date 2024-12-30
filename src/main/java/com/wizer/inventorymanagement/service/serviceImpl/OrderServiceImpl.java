package com.wizer.inventorymanagement.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wizer.inventorymanagement.dto.OrderItemRequest;
import com.wizer.inventorymanagement.dto.OrderRequest;
import com.wizer.inventorymanagement.dto.OrderResponse;
import com.wizer.inventorymanagement.model.Order;
import com.wizer.inventorymanagement.model.OrderItems;
import com.wizer.inventorymanagement.model.Product;
import com.wizer.inventorymanagement.repository.OrderRepository;
import com.wizer.inventorymanagement.repository.ProductRepository;
import com.wizer.inventorymanagement.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "order_topic";

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }


    /**
     * Places an order by validating product availability, calculating total price,
     * updating stock, saving the order, and publishing order details to Kafka.
     *
     * @param orderRequest the order request containing customer details and items
     * @return OrderResponse the response containing order details
     * @throws IllegalArgumentException if a product is not found or there is not enough stock
     */
    @Override
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        BigDecimal totalOrderPrice = BigDecimal.ZERO;
        List<OrderItems> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product with ID " + itemRequest.getProductId() + " not found"));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock for product " + product.getProductName());
            }

            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);

            BigDecimal itemTotalPrice = product.getProductPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalOrderPrice = totalOrderPrice.add(itemTotalPrice);

            OrderItems orderItem = new OrderItems(product, itemRequest.getQuantity(), itemTotalPrice);
            orderItems.add(orderItem);
        }

        Order order = new Order(orderRequest.getCustomerName(), orderRequest.getCustomerPhone(), totalOrderPrice, orderItems);
        order = orderRepository.save(order);

        // Publish order details to Kafka
        try {
            String orderMessage = objectMapper.writeValueAsString(order);
            kafkaTemplate.send(TOPIC, orderMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getCustomerPhone(),
                orderItems.stream().map(item -> item.getProduct().getProductName()).collect(Collectors.joining(", ")),
                totalOrderPrice,
                order.getOrderDate()
        );
    }


    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(this::mapToResponse);
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerName(order.getCustomerName());
        response.setCustomerPhone(order.getCustomerPhone());
        response.setProductNames(order.getProduct().getProductName());
        response.setTotalPrice(order.getTotalPrice());
        response.setOrderDate(order.getOrderDate());
        return response;
    }
}
