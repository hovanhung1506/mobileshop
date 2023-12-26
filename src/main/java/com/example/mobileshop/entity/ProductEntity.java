package com.example.mobileshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int price;
    private int quantity;
    private String photo;
    private boolean isHidden;

    @ManyToOne
    @JoinColumn(name = "brandID")
    private BrandEntity brandEntity;

    @OneToOne(mappedBy = "productEntity", fetch = FetchType.EAGER)
    private SpecificationEntity specificationEntity;
}
