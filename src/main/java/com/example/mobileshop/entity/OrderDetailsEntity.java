package com.example.mobileshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "orderDetails")
@Getter
@Setter
public class OrderDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int price;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "productID")
    private ProductEntity productEntity;

    @ManyToOne
    @JoinColumn(name = "orderID")
    private OrderEntity orderEntity;
}
