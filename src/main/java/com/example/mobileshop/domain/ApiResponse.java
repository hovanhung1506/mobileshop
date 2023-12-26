package com.example.mobileshop.domain;

import lombok.Data;

import java.util.Map;

@Data
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private Map<String, String> errors;
}
