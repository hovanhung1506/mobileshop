package com.example.mobileshop.repository;

import com.example.mobileshop.entity.CartEntity;
import com.example.mobileshop.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    CartEntity findByCustomerEntity(CustomerEntity customer);
}
