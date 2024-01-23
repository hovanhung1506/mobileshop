package com.example.mobileshop.domain;

import lombok.Data;

@Data
public class Brand {
    private Long id;
    private String name;
    private String origin;
    private int revenue;
}
