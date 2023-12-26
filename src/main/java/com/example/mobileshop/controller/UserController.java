package com.example.mobileshop.controller;

import com.example.mobileshop.domain.ApiResponse;
import com.example.mobileshop.domain.Customer;
import com.example.mobileshop.security.UserPrincipal;
import com.example.mobileshop.service.CartService;
import com.example.mobileshop.service.CustomerService;
import com.example.mobileshop.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerService customerService;

    @GetMapping
    @PostMapping
    public String index() {
        return "redirect:/user/profile";
    }


    @GetMapping("profile")
    public String profilePage(Model model) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        model.addAttribute("role",  SecurityUtil.getCurrentRole().toString());
        model.addAttribute("auth", auth);
        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("cart", cartService.cart(auth.getId()));
        return "user/profile";
    }

    @PostMapping("profile")
    public String profile(@ModelAttribute Customer customer,
                          RedirectAttributes redirect) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        Map<String, Object> errors = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        boolean flag = false;

        if(customer.getName() == null || customer.getName().isEmpty()) {
            errors.put("name", "Họ tên không được để rỗng");
            flag = true;
        }

        if(customer.getEmail() == null || customer.getEmail().isEmpty()) {
            errors.put("email", "Email không được để rỗng");
            flag = true;
        }else if(customerService.getByEmail(customer.getEmail()) != null &&
                !Objects.equals(customerService.getByEmail(customer.getEmail()).getId(), auth.getId())){
            errors.put("email","Email này đã được sử dụng");
            data.put("email",customer.getEmail());
            flag = true;
        }

        if(customer.getPhone() == null || customer.getPhone().isEmpty()) {
            errors.put("phone", "Số điện thoại không được để rỗng");
            flag = true;
        } else if (customerService.getByPhone(customer.getPhone()) != null &&
                !Objects.equals(customerService.getByPhone(customer.getPhone()).getId(), auth.getId())) {
            errors.put("phone", "Số điện thoại này đã được sử dụng");
            data.put("phone", customer.getPhone());
            flag = true;
        }

        if(!flag) {
            customer.setId(auth.getId());
            customerService.update(customer);
            redirect.addFlashAttribute("status","Cập nhật thông tin thành công!");
        }else {
            data.put("name", customer.getName());
            data.put("email",customer.getEmail());
            data.put("phone", customer.getPhone());
            redirect.addFlashAttribute("data", data);
            redirect.addFlashAttribute("errors", errors);
        }
        return "redirect:/user/profile";
    }

    @GetMapping("password")
    public String passwordPage(Model model) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        model.addAttribute("role",  SecurityUtil.getCurrentRole().toString());
        model.addAttribute("auth", auth);
        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("cart", cartService.cart(auth.getId()));
        return "user/changePassword";
    }

    @PostMapping("password")
    public String password(Model model,
                           @RequestParam String oldPassword,
                           @RequestParam String newPassword,
                           RedirectAttributes redirect) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();

        if(!customerService.checkPassword(auth.getId(), oldPassword)) {
            redirect.addFlashAttribute("status", "Mật khẩu cũ không đúng");
            return "redirect:/user/password";
        }else {
            Customer customer = new Customer();
            customer.setId(auth.getId());
            customer.setPassword(newPassword);
            customerService.update(customer);
            redirect.addFlashAttribute("status", "Đổi mật khẩu thành công");
            return "redirect:/user/profile";
        }
    }

    @PostMapping("change-image")
    @ResponseBody
    public ResponseEntity<?> changeImage(@RequestParam String photo) {
        ApiResponse<Object> res = new ApiResponse<>();
        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        Customer customer = new Customer();
        customer.setId(auth.getId());
        customer.setPhoto(photo);
        customerService.update(customer);
        res.setStatus("200");
        res.setMessage("Cập nhật ảnh đại diện thành công");
        return ResponseEntity.ok(res);
    }

    @GetMapping("address")
    public String addressPage(Model model) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        model.addAttribute("role",  SecurityUtil.getCurrentRole().toString());
        model.addAttribute("auth", auth);
        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("cart", cartService.cart(auth.getId()));
        return "user/changeAddress";
    }

    @PostMapping("address")
    @ResponseBody
    public ResponseEntity<?> address(@RequestBody Customer customer) {
        ApiResponse<Object> res = new ApiResponse<>();
        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        customer.setId(auth.getId());
        customerService.update(customer);
        res.setStatus("200");
        res.setMessage("Cập nhật địa chỉ mới thành công");
        return ResponseEntity.ok(res);
    }

}
