package com.example.mobileshop.domain;

import lombok.Data;

@Data
public class OrderDetails {
    private Long id;
    private Product product;
    private int price;
    private Long orderID;
    private int quantity;

}
