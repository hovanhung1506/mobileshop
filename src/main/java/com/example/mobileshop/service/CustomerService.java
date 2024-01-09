package com.example.mobileshop.service;

import com.example.mobileshop.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    Customer create (Customer customer);
    Customer getByUserName(String username);
    Customer getByEmail(String email);
    Customer getByPhone(String phone);
    Customer getByUserNameAndPassword(String username, String password);
    void update(Customer customer);
    boolean checkPassword(Long customerID, String oldPassword);
    Page<Customer> findAllByNameOrAddressOrEmailOrPhone(String search, Pageable pageable);
    Customer getById(Long customerID);
}
