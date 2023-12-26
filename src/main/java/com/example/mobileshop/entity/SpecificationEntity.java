package com.example.mobileshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "specifications")
@Getter
@Setter
public class SpecificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String screenSize;
    private String screenType;
    private String screenResolution;
    private String rearCamera;
    private String frontCamera;
    private String chipset;
    private String ramCapacity;
    private String storageCapacity;
    private String pin;
    private String sim;
    private String os;
    private String cpuType;
    private String size;

    @OneToOne
    @JoinColumn(name = "productID")
    private ProductEntity productEntity;
}
