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
    List<ProductEntity> findAllByBrandEntity(BrandEntity brandEntity);

    @Query("select count(*) from ProductEntity p where '' = ?1 or p.name like %?1% or p.brandEntity.name like %?1%")
    int count(String name);

    ProductEntity findByNameIgnoreCase(String name);

    @Query("select p from ProductEntity p where '' = ?1 or p.name like %?1% or p.brandEntity.name like %?1%")
    Page<ProductEntity> findByProductNameAndBrandName(String name, Pageable pageable);
}
