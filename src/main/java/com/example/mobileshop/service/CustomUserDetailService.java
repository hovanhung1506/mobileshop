package com.example.mobileshop.service;

import com.example.mobileshop.entity.CustomerEntity;
import com.example.mobileshop.repository.CustomerRepository;
import com.example.mobileshop.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomerEntity entity = customerRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Username not found"));
        UserPrincipal principal = new UserPrincipal();
        principal.setId(entity.getId());
        principal.setAuthorities(List.of(new SimpleGrantedAuthority(entity.getRole())));
        principal.setUsername(username);
        principal.setPassword(entity.getPassword());
        return principal;
    }
}
