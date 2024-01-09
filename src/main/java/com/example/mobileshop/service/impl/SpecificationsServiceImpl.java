package com.example.mobileshop.service.impl;

import com.example.mobileshop.domain.Specifications;
import com.example.mobileshop.entity.ProductEntity;
import com.example.mobileshop.entity.SpecificationEntity;
import com.example.mobileshop.repository.ProductRepository;
import com.example.mobileshop.repository.SpecificationsRepository;
import com.example.mobileshop.service.ProductService;
import com.example.mobileshop.service.SpecificationsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecificationsServiceImpl implements SpecificationsService {

    @Autowired
    private SpecificationsRepository specificationsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepository productRepository;


    @Override
    public Specifications getByProductId(Long id) {
        SpecificationEntity entity = specificationsRepository.findByProductEntityId(id);
        if (entity == null) {
            Specifications spec = new Specifications();
            spec.setId(0L);
            return spec;
        }
        return modelMapper.map(entity, Specifications.class);
    }

    @Override
    public void save(Specifications spec) {
        ProductEntity productEntity = productRepository.findById(spec.getProductId()).orElse(null);
        SpecificationEntity entity = modelMapper.map(spec, SpecificationEntity.class);
        entity.setProductEntity(productEntity);
        specificationsRepository.save(entity);
    }
}
