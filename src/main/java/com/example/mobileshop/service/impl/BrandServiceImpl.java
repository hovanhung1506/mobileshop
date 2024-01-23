package com.example.mobileshop.service.impl;

import com.example.mobileshop.domain.Brand;
import com.example.mobileshop.entity.BrandEntity;
import com.example.mobileshop.repository.BrandRepository;
import com.example.mobileshop.service.BrandService;
import com.example.mobileshop.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private OrderService orderService;

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
    public Brand getById(Long id) {
        return modelMapper.map(brandRepository.findById(id).orElse(null), Brand.class);
    }

    @Override
    public Page<Brand> findAllByNameOrOrigin(String name, Pageable page) {
        Page<BrandEntity> list = brandRepository.findAllByNameContainingOrOriginContaining(name, name, page);
        return list.map(brand -> modelMapper.map(brand, Brand.class));
    }

    @Override
    public Brand save(Brand brand) {
        BrandEntity entity = modelMapper.map(brand, BrandEntity.class);
        entity = brandRepository.save(entity);
        return modelMapper.map(entity, Brand.class);
    }

    @Override
    public boolean inUsed(Long brandID) {
        return brandRepository.isUsed(brandID);
    }

    @Override
    public void delete(Long brandID) {
        brandRepository.deleteById(brandID);
    }

    @Override
    public Brand findByName(String name) {
        return modelMapper.map(brandRepository.findByNameIgnoreCase(name), Brand.class);
    }
}
