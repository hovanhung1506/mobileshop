package com.example.mobileshop.controller;


import com.example.mobileshop.domain.ApiResponse;
import com.example.mobileshop.domain.Customer;
import com.example.mobileshop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("register")
    public String registerPage() {
        return "user/register";
    }

    @PostMapping("register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody Customer customer) {
        ApiResponse<Object> res = new ApiResponse<>();
        Map<String, String> errors = new HashMap<>();
        boolean flagErrors = false;
        if(customerService.getByUserName(customer.getUsername()) != null) {
            flagErrors = true;
            errors.put("username", "Tên đăng nhập này đã được sử dụng");
        }
        if(customerService.getByEmail(customer.getEmail()) != null) {
            flagErrors = true;
            errors.put("email", "Email này đã được sử dụng");
        }
        if(customerService.getByPhone(customer.getPhone()) != null) {
            flagErrors = true;
            errors.put("phone", "Số điện thoại này đã được sử dụng");
        }

        if(flagErrors) {
            res.setStatus("Failed");
            res.setMessage("Có lỗi");
            res.setErrors(errors);
        }else {
            customer = customerService.create(customer);
            if(customer != null) {
                res.setStatus("Success");
                res.setMessage("Tạo tài khoản thành công");
            }
        }

        return ResponseEntity.ok(res);
    }

    @GetMapping("login")
    public String loginPage() {
        return "user/login";
    }

    @PostMapping("login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestParam String username,
                                   @RequestParam String password) {
        ApiResponse<Object> res = new ApiResponse<>();
        Customer customer = customerService.getByUserNameAndPassword(username, password);
        System.out.println(customer);
        if(customer == null) {
            res.setStatus("Failed");
            res.setMessage("Sai tài khoản hoặc mật khẩu");
        }else {
            res.setStatus("OK");
        }
        return ResponseEntity.ok(res);
    }

}
