package com.example.mobileshop.controller;

import com.example.mobileshop.domain.Brand;
import com.example.mobileshop.domain.Cart;
import com.example.mobileshop.domain.Product;
import com.example.mobileshop.security.UserPrincipal;
import com.example.mobileshop.service.*;
import com.example.mobileshop.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SpecificationsService specificationsService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("{productName}")
    public String get(@PathVariable String productName,
                      Model model) {

        UserPrincipal auth = null;
        if(SecurityUtil.isAuthenticated()) {
            auth = SecurityUtil.getCurrentUser();
            Cart cart = cartService.cart(auth.getId());
            model.addAttribute("role",  SecurityUtil.getCurrentRole().toString());
            model.addAttribute("cart",cart);
            model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        }

        Product product = productService.findByName(productName.trim());
        Brand brand = brandService.getById(product.getBrandID());
        model.addAttribute("product", product);
        model.addAttribute("brand", brand);
        model.addAttribute("spec", specificationsService.getByProductId(product.getId()));
        model.addAttribute("auth", auth);
        return "product/details";
    }
}
