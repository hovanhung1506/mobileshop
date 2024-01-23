package com.example.mobileshop.controller.admin;

import com.example.mobileshop.domain.ApiResponse;
import com.example.mobileshop.security.UserPrincipal;
import com.example.mobileshop.service.BrandService;
import com.example.mobileshop.service.CustomerService;
import com.example.mobileshop.service.OrderService;
import com.example.mobileshop.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/statistic")
public class StatisticController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BrandService brandService;

    @GetMapping
    public String index(Model model) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();

        int year = LocalDateTime.now().getYear();

        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("title", "Thống kê");

        model.addAttribute("currentYear", year);
        model.addAttribute("years", orderService.listYears());
        return "admin/statistic/index";
    }

    @PostMapping("revenue")
    @ResponseBody
    public ResponseEntity<?> chartRevenue(@RequestParam(defaultValue = "1") int year) {
        if(year == 1) {
            year = LocalDateTime.now().getYear();
        }
        ApiResponse<Object> response = new ApiResponse<>();
        response.setStatus("00");
        response.setData(orderService.revenueByYear(year));
        return ResponseEntity.ok(response);
    }

    @PostMapping("brand")
    @ResponseBody
    public ResponseEntity<?> chartBrand(@RequestParam(defaultValue = "1") int year) {
        if(year == 1) {
            year = LocalDateTime.now().getYear();
        }
        ApiResponse<Object> response = new ApiResponse<>();
        response.setStatus("00");
        response.setData(orderService.revenueByBrandAndYear(year));
        return ResponseEntity.ok(response);
    }
}
