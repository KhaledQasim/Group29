package com.team29.backend.controller;

import com.team29.backend.exception.UserNotFoundE;
import com.team29.backend.model.OrderNew;
import com.team29.backend.repository.OrderNewRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;







@RestController
// TO DO , place exact url of frontend server when ready to deploy
@RequestMapping
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"}, allowCredentials = "true")
public class OrderNewController {
    @Autowired
    private OrderNewRepository orderNewRepository;
    
    @GetMapping("/test")
    String test(){
        return "works yay!";
    }
    
    @PostMapping("/loggedUser/OrderNew")
    OrderNew newOrder(@RequestBody OrderNew orderNew){
        return orderNewRepository.save(orderNew);
    }

     
    @GetMapping("/api/getAllOrders")
    List<OrderNew> GetAllOrders(){
        return orderNewRepository.findAll();
    }

    @GetMapping("/api/getAllOrders/byID/{email}")
    ArrayList<OrderNew> GetAllOrdersByID(@PathVariable String email){
       ArrayList <OrderNew> orderNew = new ArrayList<>();
       for (OrderNew temp : orderNewRepository.findAll()) {
            if (temp.getEmail().equals(email)) {
                orderNew.add(temp);
            }
        }
        return orderNew;
        
    }


    @PutMapping("/api/order/setOrderStatus/{id}")
    ResponseEntity<Object> updateOrderStatus(@RequestBody OrderNew orderNew, @PathVariable Long id) {
        orderNewRepository.findById(id)
                .map(order -> {
                    order.setOrderStatus(orderNew.getOrderStatus());
               
              
                    return orderNewRepository.save(order);
                }).orElseThrow(() ->  new UserNotFoundE("not found user"));
        return ResponseEntity.ok().build();
    }

 
       
    @GetMapping("/api/order/new")
    Integer getOrderNew(){
        ArrayList<OrderNew> New = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        LocalDate currentDateMinus1Day = currentDate.minusDays(1);
        LocalDate currentDatePlus1Day = currentDate.plusDays(1);
        for (OrderNew temp : orderNewRepository.findAll()) {
            if ( (temp.getCreatedAt().getDayOfMonth() == (currentDate.getDayOfMonth())  && temp.getCreatedAt().getYear() == currentDate.getYear()  && temp.getCreatedAt().getMonth().equals(currentDate.getMonth()))){
                New.add(temp);
            }
        }
        return New.size();
                
    }

           
    @GetMapping("/api/order/new/month")
    ArrayList<OrderNew> getOrderNewMonth(){
        ArrayList<OrderNew> New = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
    
        LocalDate currentDateMinus1Month = currentDate.minusMonths(1);
        LocalDate currentDatePlus1Month = currentDate.plusMonths(1);
        for (OrderNew temp : orderNewRepository.findAll()) {
            if (temp.getCreatedAt().getMonth().equals(currentDate.getMonth())  && temp.getCreatedAt().getYear() == currentDate.getYear()  ) {
                New.add(temp);
            }
        }
        return New;
                
    }

    @GetMapping("/api/order/number/get")
    Integer getAllOrdersAmount() {
        return orderNewRepository.findAll().size();
    }

  
}
   
