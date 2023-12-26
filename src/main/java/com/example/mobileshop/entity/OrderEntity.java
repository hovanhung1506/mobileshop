package com.example.mobileshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate;
    private boolean isPaid;
    private LocalDateTime paymentDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "customerID")
    private CustomerEntity customerEntity;

    @OneToMany(mappedBy = "orderEntity", fetch = FetchType.EAGER)
    private List<OrderDetailsEntity> orderDetailsEntities;
}
