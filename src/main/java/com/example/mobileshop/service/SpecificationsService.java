package com.example.mobileshop.service;

import com.example.mobileshop.domain.Specifications;

public interface SpecificationsService {
    Specifications getByProductId(Long id);
    void save(Specifications spec);
}
