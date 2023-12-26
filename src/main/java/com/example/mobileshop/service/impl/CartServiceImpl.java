package com.example.mobileshop.service.impl;

import com.example.mobileshop.domain.Cart;
import com.example.mobileshop.domain.CartItem;
import com.example.mobileshop.domain.Customer;
import com.example.mobileshop.domain.Product;
import com.example.mobileshop.entity.CartEntity;
import com.example.mobileshop.entity.CartItemEntity;
import com.example.mobileshop.entity.CustomerEntity;
import com.example.mobileshop.entity.ProductEntity;
import com.example.mobileshop.repository.CartItemRepository;
import com.example.mobileshop.repository.CartRepository;
import com.example.mobileshop.repository.CustomerRepository;
import com.example.mobileshop.repository.ProductRepository;
import com.example.mobileshop.security.UserPrincipal;
import com.example.mobileshop.service.CartService;
import com.example.mobileshop.utils.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void add(Product product) {
        UserPrincipal user = SecurityUtil.getCurrentUser();
        CustomerEntity customerEntity = customerRepository.findById(user.getId()).orElse(null);
        CartEntity cartEntity = cartRepository.findByCustomerEntity(customerEntity);

        if(cartEntity == null) {
            cartEntity = new CartEntity();
            cartEntity.setCustomerEntity(customerEntity);
            cartEntity = cartRepository.save(cartEntity);
            addCartItem(product, cartEntity);
        }else {
            boolean isFound = Optional.of(cartEntity.getCartItemEntities())
                            .orElse(new ArrayList<>())
                            .stream()
                            .anyMatch(item ->Objects.equals(item.getProductEntity().getId(), product.getId()));
            if(isFound) {
                cartEntity.getCartItemEntities().forEach((item) -> {
                    if(Objects.equals(item.getProductEntity().getId(), product.getId())) {
                        item.setQuantity(item.getQuantity() + product.getQuantity());
                        cartItemRepository.save(item);
                    }
                });
            }else {
                addCartItem(product, cartEntity);
            }
        }

    }

    @Override
    public Cart cart(Long customerID) {
        CustomerEntity customerEntity = customerRepository.findById(customerID).orElse(null);
        CartEntity cartEntity = cartRepository.findByCustomerEntity(customerEntity);
        Cart cart = new Cart();
        if(cartEntity == null) {
            return cart;
        }
        List<CartItem> cartItems = cartEntity.getCartItemEntities()
                        .stream()
                        .map((item) -> {
                            CartItem cartItem = new CartItem();
                            cartItem.setId(item.getId());
                            cartItem.setProduct(modelMapper.map(item.getProductEntity(), Product.class));
                            cartItem.setPrice(item.getPrice());
                            cartItem.setQuantity(item.getQuantity());
                            return cartItem;
                        }).toList();
        cart.setId(cartEntity.getId());
        cart.setId(cartEntity.getId());
        cart.setCustomer(modelMapper.map(customerEntity, Customer.class));
        cart.setCreatedAt(cartEntity.getCreatedAt());
        cart.setCartItems(cartItems);
        return cart;
    }

    @Override
    public void changeQuantity(Long productID, int quantity, Long customerID) {
        CustomerEntity customerEntity = customerRepository.findById(customerID).orElse(null);
        CartEntity cartEntity = cartRepository.findByCustomerEntity(customerEntity);
        cartEntity.getCartItemEntities().forEach((item) -> {
            if(Objects.equals(item.getProductEntity().getId(), productID)) {
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            }
        });

    }

    @Override
    public void delete(Long productID, Long customerID) {
        CustomerEntity customerEntity = customerRepository.findById(customerID).orElse(null);
        CartEntity cartEntity = cartRepository.findByCustomerEntity(customerEntity);
        ProductEntity productEntity = productRepository.findById(productID).orElse(null);
        cartItemRepository.delete(cartItemRepository.findByCartEntityAndProductEntity(cartEntity, productEntity));
    }

    @Override
    public void deleteAll(Long customerID) {
        CustomerEntity customerEntity = customerRepository.findById(customerID).orElse(null);
        CartEntity cartEntity = cartRepository.findByCustomerEntity(customerEntity);
        List<CartItemEntity> cartItemEntities = cartEntity.getCartItemEntities();
        cartItemRepository.deleteAll(cartItemEntities);
    }

    public void addCartItem(Product product, CartEntity cartEntity) {
        ProductEntity productEntity = productRepository.findById(product.getId()).orElse(null);
        CartItemEntity cartItemEntity = new CartItemEntity();
        cartItemEntity.setCartEntity(cartEntity);
        cartItemEntity.setProductEntity(productEntity);
        assert productEntity != null;
        cartItemEntity.setPrice(productEntity.getPrice());
        cartItemEntity.setQuantity(product.getQuantity());
        cartItemRepository.save(cartItemEntity);
    }
}
