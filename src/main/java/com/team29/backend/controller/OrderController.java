package com.team29.backend.controller;

import com.team29.backend.config.OrderService;
import com.team29.backend.payload.OrderDto;
import com.team29.backend.payload.OrderRequest;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = {"http://localhost:3000/","http://localhost:8080/%22%7D"}, allowCredentials = "true")

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    


    @PostMapping("/")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderRequest orderReq,Principal p ){
        String username=p.getName();
        OrderDto order = this.orderService.orderCreate(orderReq,username);
        return new ResponseEntity<OrderDto>(order,HttpStatus.CREATED)  ;
    }
    
}
