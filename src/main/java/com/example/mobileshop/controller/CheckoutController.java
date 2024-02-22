package com.example.mobileshop.controller;

import com.example.mobileshop.domain.Customer;
import com.example.mobileshop.domain.Order;
import com.example.mobileshop.domain.PaymentOS;
import com.example.mobileshop.domain.PaymentOSData;
import com.example.mobileshop.security.UserPrincipal;
import com.example.mobileshop.service.CartService;
import com.example.mobileshop.service.CustomerService;
import com.example.mobileshop.service.OrderDetailsService;
import com.example.mobileshop.service.OrderService;
import com.example.mobileshop.utils.SecurityUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailsService orderDetailsService;

    private final PaymentOS paymentOS;

    public CheckoutController(PaymentOS paymentOS) {
        this.paymentOS = paymentOS;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/payment/success")
    public String success() {
        return "cart/success";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/payment/cancel")
    public String cancel() {
        return "cart/cancel";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/payment")
    public String checkout(@RequestParam Boolean cancel,
                           @RequestParam Long orderCode,
                           @RequestParam String status) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        Order order = new Order();
        Customer customer = new Customer();
        customer.setId(auth.getId());
        order.setId(orderCode);
        order.setCustomer(customer);
        order.setStatus(status);
        orderService.save(order);
        return cancel ? "redirect:/payment/cancel" : "redirect:/payment/success";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/checkout")
    public String index(Model model) {
        UserPrincipal auth  = SecurityUtil.getCurrentUser();
        model.addAttribute("role",  SecurityUtil.getCurrentRole().toString());
        model.addAttribute("user", customerService.getByUserName(auth.getUsername()));
        model.addAttribute("auth", auth);
        model.addAttribute("cart", cartService.cart(auth.getId()));
        return "cart/checkout";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/checkout", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void checkout(HttpServletResponse httpServletResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UserPrincipal auth  = SecurityUtil.getCurrentUser();
            Order order = new Order();
            Customer customer = new Customer();
            customer.setId(auth.getId());
            order.setId(0L);
            order.setCustomer(customer);
            order.setStatus("PENDING");
            order = orderService.save(order);
            cartService.deleteAllByCustomerID(order.getCustomer().getId());
            PaymentOSData paymentData = paymentOS.getPaymentData(order);
            JsonNode data = paymentOS.createPaymentLink(paymentData);

            String checkoutUrl = data.get("checkoutUrl").asText();

            httpServletResponse.setHeader("Location", checkoutUrl);
            httpServletResponse.setStatus(302);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    @RequestMapping(method = RequestMethod.POST, value = "/checkout", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public void checkout(HttpServletResponse httpServletResponse) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            UserPrincipal auth  = SecurityUtil.getCurrentUser();
//            Order order = new Order();
//            Customer customer = new Customer();
//            customer.setId(auth.getId());
//            order.setId(0L);
//            order.setCustomer(customer);
//            order.setStatus("PENDING");
//            order = orderService.save(order);
//            cartService.deleteAllByCustomerID(order.getCustomer().getId());
//            PaymentData paymentData = getPaymentData(order);
//            JsonNode data = payOS.createPaymentLink(paymentData);
//
//            String checkoutUrl = data.get("checkoutUrl").asText();
//
//            httpServletResponse.setHeader("Location", checkoutUrl);
//            httpServletResponse.setStatus(302);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private PaymentData getPaymentData(Order order) {
//        final String description = "Thanh toan don hang: " + order.getId();
//        final String returnUrl = "http://localhost:8080/checkout/payment";
//        final String cancelUrl = "http://localhost:8080/checkout/payment";
//        final int price = 2000;
//        // Gen order code
////        String currentTimeString = String.valueOf(new Date().getTime());
//        int orderCode = order.getId().intValue();
//        List<ItemData> itemList = new ArrayList<>();
//        for(OrderDetails details : orderDetailsService.findByOrderId(order.getId())) {
//            ItemData item = new ItemData(details.getProduct().getName(), details.getQuantity(), details.getPrice());
//            itemList.add(item);
//        }
//
//        return new PaymentData( orderCode, price, description,
//                itemList, cancelUrl, returnUrl);
//    }
}
