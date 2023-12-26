package com.example.mobileshop.repository;

import com.example.mobileshop.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Long> {
    BrandEntity findByNameContainingIgnoreCase(String name);
}
