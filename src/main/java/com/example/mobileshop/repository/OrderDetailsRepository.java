package com.example.mobileshop.repository;

import com.example.mobileshop.entity.OrderDetailsEntity;
import com.example.mobileshop.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetailsEntity, Long> {

    List<OrderDetailsEntity> findAllByOrderEntity(OrderEntity orderEntity);

}
