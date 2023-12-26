package com.example.mobileshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "brands")
@Getter
@Setter
public class BrandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String origin;

    @OneToMany(mappedBy = "brandEntity", fetch = FetchType.EAGER)
    private List<ProductEntity> productEntities;
}
