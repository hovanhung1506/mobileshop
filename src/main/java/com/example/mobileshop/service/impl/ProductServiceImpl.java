package com.example.mobileshop.service.impl;

import com.example.mobileshop.domain.Product;
import com.example.mobileshop.entity.BrandEntity;
import com.example.mobileshop.entity.ProductEntity;
import com.example.mobileshop.repository.BrandRepository;
import com.example.mobileshop.repository.ProductRepository;
import com.example.mobileshop.service.ProductService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<Product> list(String search, int pageNumber) {
        String sql = "select p.id, p.name, p.photo, p.price, p.quantity, p.brandid, p.is_hidden\n" +
                "from products p inner join brands b on p.brandid = b.id\n" +
                "where '' = ?1 or p.name like ?1 or b.name like ?1\n" +
                "order by p.id\n" +
                "limit ?2 offset ?3";
        Query query = entityManager.createNativeQuery(sql);
        if(!search.isEmpty()) {
            search ='%' + search + '%';
        }
        query.setParameter(1, search);
        query.setParameter(2, 10);
        query.setParameter(3, (pageNumber -1) * 10);
        List<Object[]> results = query.getResultList();
        return results.stream()
                .map(product -> {
                    Product prd = new Product();
                    prd.setId((Long)product[0]);
                    prd.setName((String)product[1]);
                    prd.setPhoto((String)product[2]);
                    prd.setPrice((int) product[3]);
                    prd.setQuantity((int)product[4]);
                    prd.setBrandID((Long)product[5]);
                    prd.setHidden((Boolean)product[6]);
                    return prd;
                }).collect(Collectors.toList());
    }

    @Override
    public Product get(String name) {
        ProductEntity entity = productRepository.findByNameIgnoreCase(name.replaceAll("-", " "));
        Product product = modelMapper.map(entity, Product.class);
        product.setBrandID(entity.getBrandEntity().getId());
        return product;
    }

    @Override
    public List<Product> findAllByBrand(String name) {
        BrandEntity brandEntity = brandRepository.findByNameContainingIgnoreCase(name);
        return productRepository.findAllByBrandEntity(brandEntity)
                .stream()
                .map(product -> modelMapper.map(product, Product.class))
                .collect(Collectors.toList());
    }

    @Override
    public int count(String search) {
        return productRepository.count(search);
    }
}
