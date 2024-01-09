package com.example.mobileshop.repository;

import com.example.mobileshop.entity.CustomerEntity;
import com.example.mobileshop.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("select od from OrderEntity od " +
            "where '' = ?1 or od.customerEntity.name like %?1% or od.customerEntity.email like %?1% " +
            "or od.customerEntity.phone like %?1% or od.customerEntity.address like %?1% ")
    Page<OrderEntity> list(String search, Pageable pageable);

    Page<OrderEntity> findAllByCustomerEntity(CustomerEntity customerEntity, Pageable pageable);
}
