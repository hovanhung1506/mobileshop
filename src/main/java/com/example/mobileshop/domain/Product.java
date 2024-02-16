package com.example.mobileshop.domain;

import lombok.Data;

@Data
public class Product {
    private Long id;
    private String name;
    private int price;
    private int quantity;
    private String photo;
    private Long brandID;
    private String brandName;
}
