package com.example.mobileshop.repository;

import com.example.mobileshop.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByUsernameIgnoreCase(String username);
    CustomerEntity findByEmailIgnoreCase(String email);
    CustomerEntity findByPhone(String phone);
    Optional<CustomerEntity> findByUsernameAndPassword(String username, String password);
}
