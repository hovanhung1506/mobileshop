package com.example.mobileshop.service.impl;

import com.example.mobileshop.domain.OrderDetails;
import com.example.mobileshop.domain.Product;
import com.example.mobileshop.entity.OrderEntity;
import com.example.mobileshop.repository.OrderDetailsRepository;
import com.example.mobileshop.repository.OrderRepository;
import com.example.mobileshop.service.OrderDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<OrderDetails> findByOrderId(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
        return orderDetailsRepository.findAllByOrderEntity(orderEntity)
                .stream()
                .map(entity -> {
                    OrderDetails details = new OrderDetails();
                    Product product = modelMapper.map(entity.getProductEntity(), Product.class);
                    product.setBrandID(entity.getProductEntity().getBrandEntity().getId());
                    details.setProduct(product);
                    details.setId(entity.getId());
                    details.setOrderID(entity.getOrderEntity().getId());
                    details.setPrice(entity.getPrice());
                    details.setQuantity(entity.getQuantity());
                    return details;
                }).toList();
    }

    @Override
    public void delete(Long orderId) {
        OrderEntity entity = orderRepository.findById(orderId).orElse(null);
        orderDetailsRepository.deleteAll(orderDetailsRepository.findAllByOrderEntity(entity));
    }
}
