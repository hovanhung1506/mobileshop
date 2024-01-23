package com.example.mobileshop.repository;

import com.example.mobileshop.domain.Brand;
import com.example.mobileshop.entity.BrandEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Long> {
    Page<BrandEntity> findAllByNameContainingOrOriginContaining(String name, String origin, Pageable pageable);

    @Query("select case when count(*) > 0 then true else false end " +
            "from ProductEntity p where p.brandEntity.id = ?1")
    boolean isUsed(Long id);

    BrandEntity findByNameIgnoreCase(String name);

}
