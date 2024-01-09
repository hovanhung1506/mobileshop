package com.example.mobileshop.repository;

import com.example.mobileshop.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByUsername(String username);
    CustomerEntity findByEmailIgnoreCase(String email);
    CustomerEntity findByPhone(String phone);
    Page<CustomerEntity> findAllByNameContainsOrAddressContainsOrEmailContainingOrPhoneContaining(String name, String address, String email, String phone, Pageable pageable);
}
