package com.team29.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team29.backend.auth.CheckoutRequest;
import com.team29.backend.repository.OrderRepository;

import jakarta.persistence.criteria.Order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/process")
    public ResponseEntity<String> processCheckout(@RequestBody CheckoutRequest checkoutRequest) {
        // Validate the checkout request
        if (checkoutRequest.getName() == null || checkoutRequest.getName().isEmpty() ||
                checkoutRequest.getAddress() == null || checkoutRequest.getAddress().isEmpty() ||
                checkoutRequest.getPhoneNumber() == null || checkoutRequest.getPhoneNumber().isEmpty()) {
            return ResponseEntity.badRequest().body("Please fill in all required fields");
        }
        
        // Create a new order in the database
        Order order = new Order(checkoutRequest.getName(), checkoutRequest.getAddress(), checkoutRequest.getPhoneNumber());
        orderRepository.save(order);

        // Return a success response
        return ResponseEntity.ok("Checkout processed successfully");
    }
}
    

