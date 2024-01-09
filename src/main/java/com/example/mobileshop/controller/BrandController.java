package com.example.mobileshop.controller;

import com.example.mobileshop.domain.Cart;
import com.example.mobileshop.domain.Product;
import com.example.mobileshop.security.UserPrincipal;
import com.example.mobileshop.service.BrandService;
import com.example.mobileshop.service.CartService;
import com.example.mobileshop.service.CustomerService;
import com.example.mobileshop.service.ProductService;
import com.example.mobileshop.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
                               @RequestParam(defaultValue = "1") int page,
                               Model model) {

        UserPrincipal auth = null;
        if(SecurityUtil.isAuthenticated()) {
            auth = SecurityUtil.getCurrentUser();
            Cart cart = cartService.cart(auth.getId());
            model.addAttribute("role",  SecurityUtil.getCurrentRole().toString());
            model.addAttribute("cart",cart);
            model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        }
        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        Page<Product> products = productService.findByProductNameOrBrandNameOrOrigin(brandName, pageRequest);
        model.addAttribute("brands", brandService.list());
        model.addAttribute("brandName", brandName);
        model.addAttribute("products", products);
        model.addAttribute("auth", auth);
        model.addAttribute("totalPage", products.getTotalPages());
        model.addAttribute("page", page);
        return "index";
    }
}
