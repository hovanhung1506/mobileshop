package com.example.mobileshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    private LocalDateTime paymentDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "customerID")
    private CustomerEntity customerEntity;

    @OneToMany(mappedBy = "orderEntity", fetch = FetchType.EAGER)
    private List<OrderDetailsEntity> orderDetailsEntities;
}
