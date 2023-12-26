package com.example.mobileshop.service.impl;

import com.example.mobileshop.domain.Brand;
import com.example.mobileshop.repository.BrandRepository;
import com.example.mobileshop.service.BrandService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Brand> list() {
        return brandRepository.findAll()
                .stream()
                .map(brand -> modelMapper.map(brand, Brand.class))
                .collect(Collectors.toList());
    }

    @Override
    public Brand getByName(String name) {
        return modelMapper.map(brandRepository.findByNameContainingIgnoreCase(name), Brand.class);
    }

    @Override
    public Brand getById(Long id) {
        return modelMapper.map(brandRepository.findById(id).orElse(null), Brand.class);
    }
}
