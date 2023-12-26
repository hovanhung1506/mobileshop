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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerService customerService;


    @GetMapping
    public String search(@RequestParam(defaultValue = "") String q,
                         @RequestParam(defaultValue = "1") int page,
                         Model model) {
        UserPrincipal auth = null;
        if(SecurityUtil.isAuthenticated()) {
            auth = SecurityUtil.getCurrentUser();
            Cart cart = cartService.cart(auth.getId());
            model.addAttribute("role",  SecurityUtil.getCurrentRole().toString());
            model.addAttribute("cart",cart);
            model.addAttribute("user",customerService.getByUserName(auth.getUsername()));
        }
        int count = productService.count(q);
        model.addAttribute("products", productService.list(q.trim(), page));
        model.addAttribute("brands", brandService.list());
        model.addAttribute("search", q);
        model.addAttribute("totalPage", (count / 10 + (count % 10 > 0 ? 1 : 0)));
        model.addAttribute("page", page);
        model.addAttribute("auth", auth);
        return "index";
    }
}
