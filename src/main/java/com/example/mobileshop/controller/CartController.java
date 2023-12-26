package com.example.mobileshop.controller;


import com.example.mobileshop.domain.ApiResponse;
import com.example.mobileshop.domain.Cart;
import com.example.mobileshop.domain.Product;
import com.example.mobileshop.security.UserPrincipal;
import com.example.mobileshop.service.CartService;
import com.example.mobileshop.service.CustomerService;
import com.example.mobileshop.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String index(Model model) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        model.addAttribute("role",  SecurityUtil.getCurrentRole().toString());
        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("auth", auth);
        model.addAttribute("cart", cartService.cart(auth.getId()));
        return "cart/index";
    }

    @PostMapping("/add")
    public String addToCart(@ModelAttribute Product product) {
        cartService.add(product);
        return "redirect:/cart";
    }

    @PostMapping("/change-quantity")
    @ResponseBody
    public ResponseEntity<?> changeQuantity(@RequestParam Long productId,
                                            @RequestParam int quantity,
                                            @RequestParam String action) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        Long customerId = auth.getId();
        Map<Object, Object> res = new HashMap<>();
        switch (action) {
            case "change-quantity" -> {
                cartService.changeQuantity(productId, quantity, customerId);
                res.put("status","OK");
                res.put("message","Cập nhật số lượng thành công.");
            }
            case "delete" -> {
                cartService.delete(productId, customerId);
            }
            default -> {
                res.put("status","ERROR");
                res.put("message","Cố lỗi xảy ra");
            }
        }
        return ResponseEntity.ok(res);
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> delete(@RequestParam String action,
                                    @RequestParam(defaultValue = "") Long productId) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        Long customerId = auth.getId();
        Map<Object, Object> res = new HashMap<>();
        switch (action) {
            case "delete" -> {
                cartService.delete(productId, customerId);
                Cart cart = cartService.cart(customerId);
                res.put("status","OK");
                res.put("message","Xóa sản phẩm thành công");
                res.put("quantity",cart.getCartItems().size());
            }
            case "delete-all" -> {
                cartService.deleteAll(customerId);
                res.put("status","OK");
                res.put("message","Xóa tất cả sản phẩm thành công");
            }
            default -> {
                res.put("status","ERROR");
                res.put("message","Cố lỗi xảy ra");
            }
        }
        return ResponseEntity.ok(res);
    }

}
