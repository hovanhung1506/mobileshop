package com.example.mobileshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "cartItem")
@Getter
@Setter
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cartID")
    private CartEntity cartEntity;

    @ManyToOne
    @JoinColumn(name = "productID")
    private ProductEntity productEntity;

    private int quantity;
    private int price;

}
