package com.example.mobileshop.controller.admin;


import com.example.mobileshop.domain.ApiResponse;
import com.example.mobileshop.domain.Customer;
import com.example.mobileshop.security.UserPrincipal;
import com.example.mobileshop.service.CustomerService;
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

@Controller
@RequestMapping("/admin/customer")
public class CustomerAdminController {

    @Autowired
    private CustomerService customerService;
    @GetMapping({"", "search"})
    public String customerPage(Model model,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "") String q) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        int pageSize = 9;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("name").ascending());
        Page<Customer> customers = customerService.findAllByNameOrAddressOrEmailOrPhone(q.trim(), pageable);

        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("customers", customers);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", customers.getTotalPages());
        model.addAttribute("title", "Danh sách đơn hàng");
//        model.addAttribute("totalElements", customers.getTotalElements());
        model.addAttribute("search", q.trim());
        return "admin/customer/index";
    }

    @PostMapping({"", "search"})
    @ResponseBody
    public ResponseEntity<?> customerSearch(@RequestParam int page,
                                            @RequestParam(defaultValue = "", required = false) String q) {
        ApiResponse<Object> res = new ApiResponse<>();
        Pageable pageable = PageRequest.of(page - 1, 9, Sort.by("name").ascending());
        Page<Customer> customers = customerService.findAllByNameOrAddressOrEmailOrPhone(q.trim(), pageable);
        res.setStatus("200");
        res.setData(customers);
        return ResponseEntity.ok(res);
    }

    @GetMapping("create")
    public String createPage(Model model) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();

        Customer customer = new Customer();
        customer.setId(0L);
        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("title", "Tạo khách hàng mới");
        model.addAttribute("customer", customer);
        return "admin/customer/create";
    }

    @GetMapping("edit/{customerID}")
    public String editPage(Model model,
                           @PathVariable Long customerID) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("title", "Cập nhật thông tin khách hàng");
        model.addAttribute("customer",customerService.getById(customerID));

        return "admin/customer/create";
    }

    @GetMapping("view/{customerID}")
    public String view(Model model,
                       @PathVariable Long customerID) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("title", "Xem thông tin khách hàng");
        model.addAttribute("customer",customerService.getById(customerID));
        return "admin/customer/view";
    }

}
