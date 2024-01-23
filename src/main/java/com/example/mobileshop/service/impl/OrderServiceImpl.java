package com.example.mobileshop.service.impl;

import com.example.mobileshop.domain.Brand;
import com.example.mobileshop.domain.Customer;
import com.example.mobileshop.domain.Order;
import com.example.mobileshop.entity.*;
import com.example.mobileshop.repository.*;
import com.example.mobileshop.service.BrandService;
import com.example.mobileshop.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = new OrderEntity();
        CustomerEntity customerEntity = customerRepository.findById(order.getCustomer().getId()).orElse(null);
        if (order.getId() > 0) {
            orderEntity = orderRepository.findById(order.getId()).orElse(null);
            assert orderEntity != null;
            orderEntity.setStatus(order.getStatus());
            orderEntity.setPaymentDate(LocalDateTime.now());
        }
        else {
            orderEntity.setId(order.getId());
            orderEntity.setCustomerEntity(customerEntity);
            orderEntity.setStatus(order.getStatus());
            orderEntity.setOrderDate(LocalDateTime.now());
        }
        orderEntity = orderRepository.save(orderEntity);
        Order newOrder = modelMapper.map(orderEntity, Order.class);
        newOrder.setCustomer(modelMapper.map(orderEntity.getCustomerEntity(), Customer.class));

        if (order.getId() == 0) {
            CartEntity cartEntity = cartRepository.findByCustomerEntity(customerEntity);
            for (CartItemEntity item : cartEntity.getCartItemEntities()) {
                OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity();
                orderDetailsEntity.setOrderEntity(orderEntity);
                orderDetailsEntity.setProductEntity(item.getProductEntity());
                orderDetailsEntity.setPrice(item.getPrice());
                orderDetailsEntity.setQuantity(item.getQuantity());
                orderDetailsRepository.save(orderDetailsEntity);
            }
        }
        return newOrder;
    }

    @Override
    public void delete(Long orderId) {
        OrderEntity entity = orderRepository.findById(orderId).orElse(null);
        assert entity != null;
        orderDetailsRepository.deleteAll(orderDetailsRepository.findAllByOrderEntity(entity));
        orderRepository.delete(entity);
    }

    @Override
    public Page<Order> findAll(String search, int year, Pageable pageable) {
        Page<OrderEntity> entities = orderRepository.list(search, year, pageable);
        return entities.map(order ->{
            Order newOrder = new Order();
            newOrder.setId(order.getId());
            return getOrder(order, newOrder);
        });
    }

    @Override
    public Page<Order> findAllByCustomerID(Long customerID, Pageable pageable) {
        CustomerEntity customerEntity = customerRepository.findById(customerID).orElse(null);
        Page<OrderEntity> entities = orderRepository.findAllByCustomerEntity(customerEntity, pageable);
        return entities.map(order ->{
            Order newOrder = new Order();
            newOrder.setId(order.getId());
            return getOrder(order, newOrder);
        });
    }

    @Override
    public Order findByOrderId(Long orderId) {
        OrderEntity entity = orderRepository.findById(orderId).orElse(new OrderEntity());
        Order order = new Order();
        order.setId(orderId);
        return getOrder(entity, order);
    }

    @Override
    public List<Integer> listYears() {
        return orderRepository.listYears();
    }

    @Override
    public List<Integer> revenueByYear(int year) {
        List<OrderEntity> orders = orderRepository.revenueByYear(year);
        List<Integer> revenue = new ArrayList<>(Collections.nCopies(12, 0));

        orders.forEach(order -> {
            int total = order.getOrderDetailsEntities()
                    .stream()
                    .reduce(0, (a, b) -> a + b.getPrice() * b.getQuantity(), Integer::sum);
            int month = order.getOrderDate().getMonthValue() - 1;
            int sum = revenue.get(month);
            sum += total;
            revenue.set(month, sum);
        });
        return revenue;
    }

    @Override
    public List<Brand> revenueByBrandAndYear(int year) {
        List<OrderEntity> orders = orderRepository.revenueByYear(year);
        List<Brand> revenue = brandRepository.findAll().stream().map(brand -> modelMapper.map(brand, Brand.class)).toList();
        orders.forEach(order -> {
           order.getOrderDetailsEntities().forEach(detail -> {
               String brandName = detail.getProductEntity().getBrandEntity().getName();
               int sum = detail.getPrice() * detail.getQuantity();
               for(Brand brand : revenue) {
                   if(brand.getName().equals(brandName)) {
                       sum += brand.getRevenue();
                       brand.setRevenue(sum);
                       break;
                   }
               }
           });
        });
        return revenue;

    }

    private Order getOrder(OrderEntity entity, Order order) {
        order.setOrderDate(entity.getOrderDate());
        order.setPaymentDate(entity.getPaymentDate());
        order.setStatus(entity.getStatus());
        order.setCustomer(modelMapper.map(entity.getCustomerEntity(), Customer.class));
        Long total = entity.getOrderDetailsEntities()
                .stream()
                .reduce(0L, (a, b) -> a + (long) b.getPrice() * b.getQuantity(), Long::sum);
        order.setTotal(total);
        return order;
    }
}
