package com.eduardo.projarq.t1.sales.services;

import com.eduardo.projarq.t1.sales.clients.TaxServiceClient;
import com.eduardo.projarq.t1.sales.dtos.*;
import com.eduardo.projarq.t1.sales.models.Order;
import com.eduardo.projarq.t1.sales.models.OrderItem;
import com.eduardo.projarq.t1.sales.repositories.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final TaxServiceClient taxServiceClient;
    private final RabbitTemplate rabbitTemplate;
    
    public OrderService(OrderRepository orderRepository, 
                       ProductService productService, 
                       TaxServiceClient taxServiceClient,
                       RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.taxServiceClient = taxServiceClient;
        this.rabbitTemplate = rabbitTemplate;
    }
    
    public OrderDTO createOrder(CreateOrderRequestDTO request) {
        List<OrderItemDTO> orderItems = request.getItems().stream()
                .map(item -> {
                    ProductDTO product = productService.getProductByCode(item.getProductCode())
                            .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductCode()));
                    
                    OrderItemDTO orderItem = new OrderItemDTO();
                    orderItem.setProductCode(product.getCode());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setUnitPrice(product.getPrice());
                    orderItem.setTotalPrice(product.getPrice() * item.getQuantity());
                    orderItem.setEssential(product.isEssential());
                    
                    return orderItem;
                })
                .collect(Collectors.toList());
        
        double baseAmount = orderItems.stream()
                .mapToDouble(OrderItemDTO::getTotalPrice)
                .sum();
        
        TaxCalculationRequestDTO taxRequest = new TaxCalculationRequestDTO();
        taxRequest.setState(request.getState());
        taxRequest.setItems(orderItems);
        taxRequest.setBaseAmount(baseAmount);
        
        TaxCalculationResponseDTO taxResponse = taxServiceClient.calculateTaxes(taxRequest);
        
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setCustomerId(request.getCustomerId());
        order.setState(request.getState());
        order.setCountry(request.getCountry());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(taxResponse.getTotal());
        order.setTaxAmount(taxResponse.getStateTax() + taxResponse.getFederalTax());
        
        List<OrderItem> items = orderItems.stream()
                .map(item -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProductCode(item.getProductCode());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setUnitPrice(item.getUnitPrice());
                    orderItem.setTotalPrice(item.getTotalPrice());
                    orderItem.setEssential(item.isEssential());
                    return orderItem;
                })
                .collect(Collectors.toList());
        
        order.setItems(items);
        Order savedOrder = orderRepository.save(order);
        
        return convertToDTO(savedOrder);
    }
    
    public OrderDTO confirmOrder(String orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new RuntimeException("Order cannot be confirmed. Current status: " + order.getStatus());
        }
        
        for (OrderItem item : order.getItems()) {
            productService.reserveStock(item.getProductCode(), item.getQuantity());
        }
        
        order.setStatus(Order.OrderStatus.APPROVED);
        order.setApprovedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        
        OrderApprovedMessageDTO message = new OrderApprovedMessageDTO();
        message.setOrderId(order.getOrderId());
        message.setCustomerId(order.getCustomerId());
        message.setState(order.getState());
        message.setTotalAmount(order.getTotalAmount());
        message.setTaxAmount(order.getTaxAmount());
        message.setApprovedAt(order.getApprovedAt());
        
        rabbitTemplate.convertAndSend("sales.registry.queue", message);
        
        return convertToDTO(savedOrder);
    }
    
    public Optional<OrderDTO> getOrderById(String orderId) {
        return orderRepository.findByOrderId(orderId)
                .map(this::convertToDTO);
    }
    
    public List<OrderDTO> getOrdersByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByCreatedAtBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getOrderId());
        dto.setCustomerId(order.getCustomerId());
        dto.setState(order.getState());
        dto.setCountry(order.getCountry());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setTaxAmount(order.getTaxAmount());
        dto.setStatus(order.getStatus().name());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setApprovedAt(order.getApprovedAt());
        
        List<OrderItemDTO> items = order.getItems().stream()
                .map(item -> {
                    OrderItemDTO itemDto = new OrderItemDTO();
                    itemDto.setProductCode(item.getProductCode());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setUnitPrice(item.getUnitPrice());
                    itemDto.setTotalPrice(item.getTotalPrice());
                    itemDto.setEssential(item.isEssential());
                    return itemDto;
                })
                .collect(Collectors.toList());
        
        dto.setItems(items);
        return dto;
    }
} 