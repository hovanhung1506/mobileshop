package com.example.mobileshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String photo;
    private String username;
    private String password;
    private String role;

    @OneToMany(mappedBy = "customerEntity", fetch = FetchType.EAGER)
    private List<OrderEntity> orderEntities;

    @OneToOne(mappedBy = "customerEntity", fetch = FetchType.EAGER)
    private CartEntity cartEntity;
}
