package com.example.mobileshop.controller.admin;

import com.example.mobileshop.domain.ApiResponse;
import com.example.mobileshop.domain.Order;
import com.example.mobileshop.security.UserPrincipal;
import com.example.mobileshop.service.CustomerService;
import com.example.mobileshop.service.OrderDetailsService;
import com.example.mobileshop.service.OrderService;
import com.example.mobileshop.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailsService orderDetailsService;

    @GetMapping({"", "search"})
    public String index(Model model,
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "") String q,
                        @RequestParam(defaultValue = "1") int year) {

        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        int pageSize = 9;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("orderDate").descending());
//        if(year == 1) {
//            year = LocalDateTime.now().getYear();
//        }
        Page<Order> orders = orderService.findAll(q.trim(), year, pageable);


        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("title", "Danh sách đơn hàng");
        model.addAttribute("search", q.trim());

        model.addAttribute("years", orderService.listYears());
        model.addAttribute("currentYear", year);
        model.addAttribute("orders", orders);
        return "admin/order/index";
    }

    @PostMapping({"", "search"})
    @ResponseBody
    public ResponseEntity<?> search(@RequestParam int page,
                                    @RequestParam(defaultValue = "", required = false) String q,
                                    @RequestParam(defaultValue = "1", required = false) int year) {
        ApiResponse<Object> res = new ApiResponse<>();
        Pageable pageable = PageRequest.of(page - 1, 9, Sort.by("orderDate").descending());
//        if(year == 1) {
//            year = LocalDateTime.now().getYear();
//        }
        Page<Order> orders = orderService.findAll(q.trim(), year, pageable);
        res.setStatus("200");
        res.setData(orders);
        return ResponseEntity.ok(res);
    }

    @GetMapping("edit/{orderID}")
    public String edit(Model model,
                       @PathVariable Long orderID) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();

        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("title", "Xem đơn hàng " + orderID);
        model.addAttribute("order", orderService.findByOrderId(orderID));
        model.addAttribute("orderDetails",orderDetailsService.findByOrderId(orderID));
        return "admin/order/view";
    }
}
