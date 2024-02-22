package com.example.mobileshop.controller.admin;

import com.example.mobileshop.domain.ApiResponse;
import com.example.mobileshop.domain.Brand;
import com.example.mobileshop.domain.Product;
import com.example.mobileshop.domain.Specifications;
import com.example.mobileshop.security.UserPrincipal;
import com.example.mobileshop.service.BrandService;
import com.example.mobileshop.service.CustomerService;
import com.example.mobileshop.service.ProductService;
import com.example.mobileshop.service.SpecificationsService;
import com.example.mobileshop.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin/product")
public class ProductAdminController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SpecificationsService specificationsService;

    @GetMapping({"", "search"})
    public String productPage(Model model,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "") String q) {
        UserPrincipal auth = SecurityUtil.getCurrentUser();
        int pageSize = 9;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Product> products = productService.findByProductNameOrBrandNameOrOrigin(q.trim(), pageable);


        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("products", products);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalElements", products.getTotalElements());
        model.addAttribute("search", q.trim());
//        model.addAttribute("numberOfElements", products.getNumberOfElements());
        return "admin/product/index";
    }

    @PostMapping({"", "search"})
    @ResponseBody
    public ResponseEntity<?> productSearch(@RequestParam int page,
                                           @RequestParam(defaultValue = "", required = false) String q) {
        ApiResponse<Object> res = new ApiResponse<>();
        Pageable pageable = PageRequest.of(page - 1, 9);
        Page<Product> products = productService.findByProductNameOrBrandNameOrOrigin(q.trim(), pageable);
        res.setStatus("200");
        res.setData(products);
        return ResponseEntity.ok(res);
    }

    @GetMapping("create")
    public String createPage(Model model) {
        UserPrincipal auth = SecurityUtil.getCurrentUser();
        Product product = new Product();
        product.setId(0L);
        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("title", "Tạo sản phẩm mới");
        model.addAttribute("product", product);
        model.addAttribute("brands", brandService.list());
        return "admin/product/create";
    }

    @PostMapping("save")
    public String save(@ModelAttribute Product product,
                       RedirectAttributes redirect,
                       Model model) {
        Map<String, Object> errors = new HashMap<>();
        if (product.getName() == null || product.getName().isEmpty()) {
            errors.put("name", "Tên sản phẩm là bắt buộc");
        }
        if (product.getQuantity() < 1) {
            errors.put("quantity", "Số lượng phải lớn hơn 1");
        }
        if (product.getPrice() < 1) {
            errors.put("price", "Số tiền phải lớn hơn 1 đồng");
        }
        if (product.getBrandID() == null || product.getBrandID() < 1) {
            errors.put("brand", "Thương hiệu là bắt buộc");
        }
        if (productService.findByName(product.getName()) != null
                && !Objects.equals(productService.findByName(product.getName()).getId(), product.getId())) {
            errors.put("name", "Tên sản phẩm đã tồn tại");
        }

        if (!errors.isEmpty()) {
            redirect.addFlashAttribute("errors", errors);
            return product.getId() == 0L
                    ? "redirect:/admin/product/create"
                    : "redirect:/admin/product/edit/" + product.getId();
        }

        product.setName(product.getName().trim());
        if (product.getId() == 0) {
            redirect.addFlashAttribute("message", "Tạo mới sản phẩm thành công");
        } else {
            redirect.addFlashAttribute("message", "Cập nhật sản phẩm thành công");
        }
        if(product.getPhoto().isEmpty()) {
            product.setPhoto("images/products/No-Image.png");
        }
        product = productService.save(product);
        redirect.addFlashAttribute("newProduct", product);

        return "redirect:/admin/product";
    }

    @GetMapping("edit/{productID}")
    public String editPage(@PathVariable Long productID,
                           Model model) {

        UserPrincipal auth = SecurityUtil.getCurrentUser();
        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("brands", brandService.list());
        model.addAttribute("spec", specificationsService.getByProductId(productID));
        model.addAttribute("product", productService.findById(productID));
        model.addAttribute("title", "Cập nhật sản phẩm");
        return "admin/product/create";
    }

    @GetMapping("delete/{productID}")
    public String deletePage(@PathVariable Long productID,
                             Model model) {
        UserPrincipal auth = SecurityUtil.getCurrentUser();
        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("title", "Xóa sản phẩm");
        model.addAttribute("product", productService.findById(productID));
        model.addAttribute("isUsed", productService.isUsed(productID));
        return "admin/product/delete";
    }

    @PostMapping("delete/{productID}")
    public String delete(@PathVariable Long productID) {


        productService.delete(productID);
        return "redirect:/admin/product";
    }
}
