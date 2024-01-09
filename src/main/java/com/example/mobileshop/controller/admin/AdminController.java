package com.example.mobileshop.controller.admin;

import com.example.mobileshop.domain.ApiResponse;
import com.example.mobileshop.domain.Brand;
import com.example.mobileshop.domain.Product;
import com.example.mobileshop.security.UserPrincipal;
import com.example.mobileshop.service.BrandService;
import com.example.mobileshop.service.CustomerService;
import com.example.mobileshop.service.ProductService;
import com.example.mobileshop.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @GetMapping
    public String index() {
        return "redirect:/admin/product";
    }



}
