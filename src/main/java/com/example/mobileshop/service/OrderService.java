package com.example.mobileshop.service;

import com.example.mobileshop.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Order save(Order order);
    void delete(Long orderId);
    Page<Order> findAll(String search, Pageable pageable);
    Page<Order> findAllByCustomerID(Long customerID, Pageable pageable);
    Order findByOrderId(Long orderId);
}
