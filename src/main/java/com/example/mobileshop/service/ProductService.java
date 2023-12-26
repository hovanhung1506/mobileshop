package com.example.mobileshop.service;

import com.example.mobileshop.domain.Product;

import java.util.List;

public interface ProductService {

    List<Product> list(String search, int pageNumber);
    Product get(String name);
    List<Product> findAllByBrand(String name);
    int count(String search);
}
