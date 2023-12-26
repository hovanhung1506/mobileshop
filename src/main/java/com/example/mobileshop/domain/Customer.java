package com.example.mobileshop.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Customer {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String photo;
    private String username;
    private String password;
    private String role;
}
