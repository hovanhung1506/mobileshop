package com.example.mobileshop.domain;

import lombok.Data;

@Data
public class CartItem {
    private Long id;
    private Product product;
    private int quantity;
    private int price;
}
