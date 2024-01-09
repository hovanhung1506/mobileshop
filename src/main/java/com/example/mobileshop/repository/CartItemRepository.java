package com.example.mobileshop.repository;

import com.example.mobileshop.entity.CartEntity;
import com.example.mobileshop.entity.CartItemEntity;
import com.example.mobileshop.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    CartItemEntity findByCartEntityAndProductEntity(CartEntity cartEntity, ProductEntity productEntity);
    List<CartItemEntity> findAllByProductEntityId(Long id);
}
