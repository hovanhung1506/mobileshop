package com.example.mobileshop.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;
    private LocalDateTime orderDate;
    private boolean isPaid;
    private LocalDateTime paymentDate;
    private String status;
    private Long customerID;
}
