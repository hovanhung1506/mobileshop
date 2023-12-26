package com.example.mobileshop.service;

import com.example.mobileshop.domain.Brand;

import java.util.List;

public interface BrandService {
    List<Brand> list();
    Brand getByName(String name);
    Brand getById(Long id);

}
