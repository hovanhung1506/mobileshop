package com.example.mobileshop.service;

import com.example.mobileshop.domain.Cart;
import com.example.mobileshop.domain.Product;

public interface CartService {
    void add(Product product);
    Cart cart(Long customerID);
    void changeQuantity(Long productID, int quantity, Long customerID);
    void deleteByProductIDAndCustomerID(Long productID, Long customerID);
    void deleteAllByCustomerID(Long customerID);
}
