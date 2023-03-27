package com.team29.backend.model;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;
    private String image;
    private String description;
    private int quantity;
    private String images;
    private String category;
    @Enumerated(EnumType.STRING)
    private Size size;
    @CreationTimestamp
    private LocalDate createdAt;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart")
    @JsonIgnore
    private Cart cart;

    public Product(Long productId, double price, int quantity, Size size) {
        this.id = productId;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
    }
   
    // Getter and setter
    public Cart getCart() {
        return cart;
    }

     public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setCart(Cart cart) {
        this.cart = cart;
    }
    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
    
}
