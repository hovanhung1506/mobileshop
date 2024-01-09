package com.example.mobileshop.controller.admin;


import com.example.mobileshop.domain.Product;
import com.example.mobileshop.domain.Specifications;
import com.example.mobileshop.service.ProductService;
import com.example.mobileshop.service.SpecificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/spec")
public class SpecAdminController {

    @Autowired
    private SpecificationsService specificationsService;

    @Autowired
    private ProductService productService;

    @PostMapping
    public String save(RedirectAttributes redirect,
                       @ModelAttribute Specifications spec) {
        specificationsService.save(spec);
        Product product = productService.findById(spec.getProductId());
        redirect.addFlashAttribute("newProduct", product);
        redirect.addFlashAttribute("message", "Cập nhật sản phẩm thành công");
        return "redirect:/admin/product";
    }

}
