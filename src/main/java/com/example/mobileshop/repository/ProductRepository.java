package com.example.mobileshop.repository;

import com.example.mobileshop.entity.BrandEntity;
import com.example.mobileshop.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    ProductEntity findByNameIgnoreCase(String name);

    @Query("select p from ProductEntity p " +
            "where '' = ?1 or p.name like %?1% or p.brandEntity.name like %?1% or p.brandEntity.origin like %?1% ")
    Page<ProductEntity> findByProductNameOrBrandName(String name, Pageable pageable);

    @Query("select case when count(*) > 0 then true else false end " +
            "from OrderDetailsEntity od " +
            "where od.productEntity.id = ?1")
    boolean isUsed(Long id);
}
