package com.portfolio.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.ecommerce.model.Order;
import com.portfolio.ecommerce.service.CheckoutService;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<Order> finalizeOrder() {
        // Mockando usuário ID 1 (Alice) novamente
        Order newOrder = checkoutService.checkout(1L);
        return ResponseEntity.ok(newOrder);
    }
}