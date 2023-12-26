package com.example.mobileshop.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Cart {
    private Long id;
    private Customer customer;
    private List<CartItem> cartItems = new ArrayList<>();
    private LocalDateTime createdAt;
}
