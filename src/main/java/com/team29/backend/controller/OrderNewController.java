package com.team29.backend.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team29.backend.exception.ProductNotFoundException;
import com.team29.backend.model.OrderNew;
import com.team29.backend.model.Product;
import com.team29.backend.repository.OrderNewRepository;


@RestController
// TO DO , place exact url of frontend server when ready to deploy
@RequestMapping
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"}, allowCredentials = "true")
public class OrderNewController {
    @Autowired
    private OrderNewRepository orderNewRepository;
    
    
    @PostMapping("/loggedUser/OrderNew")
    OrderNew newOrder(@RequestBody OrderNew orderNew){
        return orderNewRepository.save(orderNew);
    }

}
   
