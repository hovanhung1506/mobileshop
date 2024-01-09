package com.example.mobileshop.service;

import com.example.mobileshop.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Product findByName(String name);
    Product findById(Long id);
    Page<Product> findByProductNameOrBrandNameOrOrigin(String name, Pageable page);
    Product save(Product product);
    boolean isUsed(Long productId);
    void delete(Long productId);
}
