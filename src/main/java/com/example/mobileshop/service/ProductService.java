package com.example.mobileshop.service;

import com.example.mobileshop.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    List<Product> list(String search, int pageNumber);
    Product get(String name);
    List<Product> findAllByBrand(String name);
    Page<Product> findByProductNameAndBrandName(String name, Pageable page);
}
