package com.example.mobileshop.controller;

import com.example.mobileshop.domain.Cart;
import com.example.mobileshop.security.UserPrincipal;
import com.example.mobileshop.service.BrandService;
import com.example.mobileshop.service.CartService;
import com.example.mobileshop.service.CustomerService;
import com.example.mobileshop.service.ProductService;
import com.example.mobileshop.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("{brandName}")
    public String getBrandName(@PathVariable String brandName,
                               Model model) {

        UserPrincipal auth = null;
        if(SecurityUtil.isAuthenticated()) {
            auth = SecurityUtil.getCurrentUser();
            Cart cart = cartService.cart(auth.getId());
            model.addAttribute("role",  SecurityUtil.getCurrentRole().toString());
            model.addAttribute("cart",cart);
            model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        }

        model.addAttribute("brands", brandService.list());
        model.addAttribute("brandName", brandName);
        model.addAttribute("products", productService.findAllByBrand(brandName));
        model.addAttribute("auth", auth);
        return "index";
    }
}
