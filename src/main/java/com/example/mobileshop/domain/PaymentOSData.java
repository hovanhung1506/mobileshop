package com.example.mobileshop.domain;

import lombok.Data;

@Data
public class PaymentOSData {
    private int orderCode;
    private int amount;
    private String description;
    private String cancelUrl;
    private String returnUrl;
    private String signature = null;

    public PaymentOSData(int orderCode, int amount, String description, String cancelUrl, String returnUrl) {
        this.orderCode = orderCode;
        this.amount = amount;
        this.description = description;
        this.cancelUrl = cancelUrl;
        this.returnUrl = returnUrl;
    }
}
