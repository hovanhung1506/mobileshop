package com.example.mobileshop.service.impl;

import com.example.mobileshop.domain.Specifications;
import com.example.mobileshop.repository.SpecificationsRepository;
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


    @Override
    public Specifications getByProductId(Long id) {
        return modelMapper.map(specificationsRepository.findByProductEntityId(id), Specifications.class);
    }
}
