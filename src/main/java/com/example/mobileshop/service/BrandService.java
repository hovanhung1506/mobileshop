package com.example.mobileshop.service;

import com.example.mobileshop.domain.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandService {
    List<Brand> list();
    Brand getById(Long id);
    Page<Brand> findAllByNameOrOrigin(String name, Pageable page);
    Brand save(Brand brand);
    boolean inUsed(Long brandID);
    void delete(Long brandID);
    Brand findByName(String name);
}
