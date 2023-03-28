package com.team29.backend.config;


import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.team29.backend.exception.CartNotFoundException;
import com.team29.backend.exception.UserEmailWrongException;
import com.team29.backend.model.Cart;
import com.team29.backend.model.Order;
import com.team29.backend.model.OrderItem;
import com.team29.backend.model.Product;
import com.team29.backend.model.User;
import com.team29.backend.payload.OrderDto;
import com.team29.backend.payload.OrderRequest;
import com.team29.backend.repository.CartRepository;
import com.team29.backend.repository.OrderRepository;
import com.team29.backend.repository.UserRepository;

@Service
public class OrderService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CartRepository cartRepo;
    
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OrderRepository orderRepo;


    //order create methods

    public OrderDto orderCreate(OrderRequest request,String username){
        User user = this.userRepo.findByEmail(username).orElseThrow(()->new UserEmailWrongException());
        int cartId = request.getCartId();
        String orderAddress = request.getOrderAddress();
        //find cart
        Cart cart = this.cartRepo.findById((long) cartId).orElseThrow(()->new CartNotFoundException(null));
        //getting cart items
        List<Product> items = cart.getProducts();


        Order order = new Order();
        AtomicReference<Double> totalOrderPrice= new AtomicReference<Double>(0.0);
       Set<OrderItem> orderItems =  items.stream().map((Product)->{
            OrderItem orderItem = new OrderItem();
            //set cart item into order item
            
            //ser product in order item
            orderItem.setProduct(Product.getName());

            //set product quantity in orderItem

            orderItem.setProductQuantity(Product.getQuantity());

            orderItem.setTotalProductprice(Product.getPrice());

            orderItem.setOrder(order);

            totalOrderPrice.set(totalOrderPrice.get()+ orderItem.getTotalProductprice());
            Long productId= orderItem.getProduct().getId();

            return orderItem;
            

        } ).collect(Collectors.toSet());

        order.setBillingAddress(orderAddress);
        order.setOrderDelivered(null);
        order.setOrderStatus("CREATED");
        order.setPaymentStatus("NOT PAID");
        order.setUser(user);
        order.setOrderItem(orderItems);
        order.setOrderAmt(totalOrderPrice.get());
        order.setOrderCreateAt(new Date());
        Order save;

        if(order.getOrderAmt()>0){
             save = this.orderRepo.save(order);
            cart.getProducts().clear();
            this.cartRepo.save(cart);
            System.out.println("Hello");

        }else{
            System.out.println(order.getOrderAmt());
            throw new CartNotFoundException(null);

        }


        cart.getProducts().clear();
        Cart savedCart = this.cartRepo.save(cart);


        return this.modelMapper.map(save,OrderDto.class);
    }
}
