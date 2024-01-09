package com.example.mobileshop.service;

import com.example.mobileshop.domain.OrderDetails;

import java.util.List;

public interface OrderDetailsService {

    List<OrderDetails> findByOrderId(Long orderId);
    void delete(Long orderId);


}
