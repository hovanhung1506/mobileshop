package com.example.mobileshop.service;

import com.example.mobileshop.domain.Brand;
import com.example.mobileshop.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    Order save(Order order);
    void delete(Long orderId);
    Page<Order> findAll(String search, int year, Pageable pageable);
    Page<Order> findAllByCustomerID(Long customerID, Pageable pageable);
    Order findByOrderId(Long orderId);
    List<Integer> listYears();
    List<Integer> revenueByYear(int year);
    List<Brand> revenueByBrandAndYear(int year);
}
