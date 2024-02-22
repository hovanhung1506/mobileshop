package com.example.mobileshop.service.impl;

import com.example.mobileshop.domain.Product;
import com.example.mobileshop.entity.BrandEntity;
import com.example.mobileshop.entity.CartItemEntity;
import com.example.mobileshop.entity.ProductEntity;
import com.example.mobileshop.entity.SpecificationEntity;
import com.example.mobileshop.repository.BrandRepository;
import com.example.mobileshop.repository.CartItemRepository;
import com.example.mobileshop.repository.ProductRepository;
import com.example.mobileshop.repository.SpecificationsRepository;
import com.example.mobileshop.service.ProductService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private ModelMapper modelMapper;

    @Autowired
    private SpecificationsRepository specificationsRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public Product findByName(String name) {
        ProductEntity entity = productRepository.findByNameIgnoreCase(name.replaceAll("-", " "));
        if (entity == null) return null;
        Product product = modelMapper.map(entity, Product.class);
        product.setBrandID(entity.getBrandEntity().getId());
        product.setBrandName(entity.getBrandEntity().getName());
        return product;
    }

    @Override
    public Product findById(Long id) {
        ProductEntity entity = productRepository.findById(id).orElse(null);
        Product product = modelMapper.map(entity, Product.class);
        assert entity != null;
        product.setBrandID(entity.getBrandEntity().getId());
        product.setBrandName(entity.getBrandEntity().getName());
        return product;
    }

    @Override
    public Page<Product> findByProductNameOrBrandNameOrOrigin(String name, Pageable page) {
        Page<ProductEntity> productEntities = productRepository.findByProductNameOrBrandName(name, page);
        return productEntities.map(product -> {
            Product p = modelMapper.map(product, Product.class);
            p.setBrandID(product.getBrandEntity().getId());
            p.setBrandName(product.getBrandEntity().getName());
            return p;
        });
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = modelMapper.map(product, ProductEntity.class);
        BrandEntity brandEntity = brandRepository.findById(product.getBrandID()).orElse(null);
        entity.setBrandEntity(brandEntity);
        entity = productRepository.save(entity);
        product = modelMapper.map(entity, Product.class);
        product.setBrandName(entity.getBrandEntity().getName());
        product.setBrandID(entity.getBrandEntity().getId());
        return product;
    }

    @Override
    public boolean isUsed(Long productId) {
        return productRepository.isUsed(productId);
    }

    @Override
    public void delete(Long productId) {
        SpecificationEntity specificationEntity = specificationsRepository.findByProductEntityId(productId);
        if(specificationEntity != null) {
            specificationsRepository.delete(specificationEntity);
        }
        List<CartItemEntity> cartItems = cartItemRepository.findAllByProductEntityId(productId);
        cartItemRepository.deleteAll(cartItems);
        productRepository.deleteById(productId);
    }
}
