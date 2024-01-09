package com.example.mobileshop.service.impl;

import com.example.mobileshop.domain.Customer;
import com.example.mobileshop.entity.CustomerEntity;
import com.example.mobileshop.repository.CustomerRepository;
import com.example.mobileshop.security.UserPrincipal;
import com.example.mobileshop.service.CustomUserDetailService;
import com.example.mobileshop.service.CustomerService;
import com.example.mobileshop.utils.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailService userDetailService;

    @Override
    public Customer create(Customer customer) {
        CustomerEntity entity = modelMapper.map(customer, CustomerEntity.class);
        if(entity.getPhoto() == null) {
            entity.setRole("images/blank-profile.png");
        }
        entity.setRole("ROLE_USER");
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setPhoto("images/blank-profile.png");
        entity = customerRepository.save(entity);
        return modelMapper.map(entity, Customer.class);
    }

    @Override
    public Customer getByUserName(String username) {
        CustomerEntity entity = customerRepository.findByUsername(username).orElse(null);
        return entity == null ? null : modelMapper.map(entity, Customer.class);
    }

    @Override
    public Customer getByEmail(String email) {
        CustomerEntity entity = customerRepository.findByEmailIgnoreCase(email);
        return entity == null ? null : modelMapper.map(entity, Customer.class);
    }

    @Override
    public Customer getByPhone(String phone) {
        CustomerEntity entity = customerRepository.findByPhone(phone);
        return entity == null ? null : modelMapper.map(entity, Customer.class);
    }

    @Override
    public Customer getByUserNameAndPassword(String username, String password) {
        CustomerEntity entity = customerRepository.findByUsername(username).orElse(null);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        System.out.println(entity != null && encoder.matches(password, entity.getPassword()));
        if(entity != null && !encoder.matches(password, entity.getPassword())) {
            entity = null;
        }
        return entity == null ? null : modelMapper.map(entity, Customer.class);
    }

    @Override
    public void update(Customer customer) {
        CustomerEntity entity = customerRepository.findById(customer.getId()).orElse(null);
        if(entity != null) {
            if(customer.getName() != null && !customer.getName().isEmpty()) {
                entity.setName(customer.getName());
            }
            if(customer.getEmail() != null && !customer.getEmail().isEmpty()) {
                entity.setEmail(customer.getEmail());
            }
            if(customer.getPhone() != null && !customer.getPhone().isEmpty()) {
                entity.setPhone(customer.getPhone());
            }
            if(customer.getAddress() != null && !customer.getAddress().isEmpty()) {
                entity.setAddress(customer.getAddress());
            }
            if(customer.getPassword() != null && !customer.getPassword().isEmpty()) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                entity.setPassword(encoder.encode(customer.getPassword()));
            }
            if(customer.getPhoto() != null && !customer.getPhoto().isEmpty()) {
                entity.setPhoto(customer.getPhoto());
            }
            customerRepository.save(entity);

        }
    }

    @Override
    public boolean checkPassword(Long customerID, String oldPassword) {
        CustomerEntity entity = customerRepository.findById(customerID).orElse(null);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return entity != null && encoder.matches(oldPassword, entity.getPassword());
    }

    @Override
    public Page<Customer> findAllByNameOrAddressOrEmailOrPhone(String search, Pageable pageable) {
        Page<CustomerEntity> entities = customerRepository.findAllByNameContainsOrAddressContainsOrEmailContainingOrPhoneContaining(search, search, search, search, pageable);
        return entities.map(customer -> modelMapper.map(customer, Customer.class));
    }

    @Override
    public Customer getById(Long customerID) {
        return modelMapper.map(customerRepository.findById(customerID), Customer.class);
    }
}
