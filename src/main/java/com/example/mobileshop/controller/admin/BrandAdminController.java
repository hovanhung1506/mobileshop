package com.example.mobileshop.controller.admin;

import com.example.mobileshop.domain.ApiResponse;
import com.example.mobileshop.domain.Brand;
import com.example.mobileshop.security.UserPrincipal;
import com.example.mobileshop.service.BrandService;
import com.example.mobileshop.service.CustomerService;
import com.example.mobileshop.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/brand")
public class BrandAdminController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CustomerService customerService;

    @GetMapping({"", "search"})
    public String getBrandPage(Model model,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "") String q) {

        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        int pageSize = 3;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Brand> brands = brandService.findAllByNameOrOrigin(q, pageable);
        model.addAttribute("page", page);
        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("brands", brands);
        model.addAttribute("totalPages", brands.getTotalPages());
        model.addAttribute("totalElements", brands.getTotalElements());
        model.addAttribute("search", q.trim());
        model.addAttribute("numberOfElements", brands.getNumberOfElements());
        model.addAttribute("title", "Danh sách thương hiệu");
        return "admin/brand/index";
    }

    @PostMapping({"", "search"})
    @ResponseBody
    public ResponseEntity<?> brandSearch(@RequestParam int page,
                                         @RequestParam(defaultValue = "", required = false) String q) {
        ApiResponse<Object> res = new ApiResponse<>();
        Pageable pageable = PageRequest.of(page - 1, 3);
        Page<Brand> brands = brandService.findAllByNameOrOrigin(q, pageable);
        res.setStatus("200");
        res.setData(brands);
        return ResponseEntity.ok(res);
    }

    @GetMapping("create")
    public String createPage(Model model) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        Brand brand = new Brand();
        brand.setId(0L);
        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("brand", brand);
        model.addAttribute("title", "Tạo thương hiệu mới");
        return "admin/brand/create";
    }

    @GetMapping("edit/{brandID}")
    public String editPage(@PathVariable Long brandID,
                           Model model) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("brand", brandService.getById(brandID));
        model.addAttribute("title", "Cập nhật thương hiệu");
        return "admin/brand/create";
    }

    @PostMapping("save")
    public String create(@ModelAttribute Brand brand,
                         Model model,
                         RedirectAttributes redirect) {
        Map<String, Object> errors = new HashMap<>();
        if(brand.getName() == null || brand.getName().isEmpty()) {
            errors.put("name", "Tên thương hiệu là bắt buộc");
        }
        if(brand.getOrigin() == null || brand.getOrigin().isEmpty()) {
            errors.put("origin", "Xuất xứ là bắt buộc");
        }
        if(brandService.findByName(brand.getName()) != null) {
            errors.put("name", "Tên thương hiệu đã tồn tại");
        }

        if(!errors.isEmpty()) {
            UserPrincipal auth  = SecurityUtil.getCurrentUser();
            redirect.addFlashAttribute("errors", errors);
            redirect.addFlashAttribute("brandError", brand);
            return "redirect:/admin/brand/create";
        }

        brand.setName(brand.getName().trim());
        brand.setOrigin(brand.getOrigin().trim());
        brand = brandService.save(brand);
        redirect.addFlashAttribute("newBrand", brand);
        if(brand.getId() == 0) {
            redirect.addFlashAttribute("message", "Tạo mới thương hiệu thành công");
        }else {
            redirect.addFlashAttribute("message", "Cập nhật thương hiệu thành công");
        }
        return "redirect:/admin/brand";
    }

    @GetMapping("delete/{brandID}")
    public String deletePage(Model model,
                             @PathVariable Long brandID) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("title", "Xóa thương hiệu");
        model.addAttribute("brand", brandService.getById(brandID));
        model.addAttribute("isUsed", brandService.inUsed(brandID));
        return "admin/brand/delete";
    }

    @PostMapping("delete/{brandID}")
    public String delete(@PathVariable Long brandID) {

        if(brandService.inUsed(brandID)) {
            return "redirect:/admin/brand/delete/" + brandID;
        }
        brandService.delete(brandID);
        return "redirect:/admin/brand";
    }

}
