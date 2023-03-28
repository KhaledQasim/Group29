package com.team29.backend.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team29.backend.model.Order;
import com.team29.backend.model.Product;
import com.team29.backend.repository.ProductRepository;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

public class OrderItemDto {
    

    private int orderItemId;
 
    private ProductRepository product;
    private double totalProductprice;
    @JsonIgnore
    private OrderDto order;

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public ProductRepository getProduct() {
        return product;
    }

    public void setProduct(ProductRepository product) {
        this.product = product;
    }

    public double getTotalProductprice() {
        return totalProductprice;
    }

    public void setTotalProductprice(double totalProductprice) {
        this.totalProductprice = totalProductprice;
    }

    public OrderDto getOrder() {
        return order;
    }

    public void setOrder(OrderDto order) {
        this.order = order;
    }

   
    
}
