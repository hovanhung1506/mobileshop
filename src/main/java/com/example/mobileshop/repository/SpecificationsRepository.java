package com.example.mobileshop.repository;

import com.example.mobileshop.entity.SpecificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecificationsRepository extends JpaRepository<SpecificationEntity, Long> {
    SpecificationEntity findByProductEntityId(Long id);
}
