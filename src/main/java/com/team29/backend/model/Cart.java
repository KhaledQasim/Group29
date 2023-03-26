package com.team29.backend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private int quantity;
    private double totalPrice;
    
    @OneToMany(mappedBy = "cart")
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        Optional<Product> existingProduct = getProductById(product.getId());
        if (existingProduct.isPresent()) {
            existingProduct.get().setQuantity(existingProduct.get().getQuantity() + product.getQuantity());
        } else {
            product.setCart(this);
            products.add(product);
        }
    }

    public void removeProduct(Long productId) {
        Optional<Product> existingProduct = getProductById(productId);
        existingProduct.ifPresent(product -> products.remove(product));
    }

    public int getProductCount() {
        int count = 0;
        for (Product product : products) {
            count += product.getQuantity();
        }
        return count * quantity;
    }

    public double getPrice() {
        totalPrice = 0.0;
        for (Product product : products) {
            totalPrice += product.getPrice() * product.getQuantity() * quantity;
        }
        return totalPrice;
    }
   
    public void setProductCount(int count) {
        int currentCount = getProductCount() / quantity;
        if (currentCount > 0) {
            double factor = (double) count / currentCount;
            for (Product product : products) {
                product.setQuantity((int) (product.getQuantity() * factor));
            }
        }
        this.quantity = count;
    }
    
    public void setTotalPrice(double totalPrice) {
        double currentPrice = getPrice();
        if (currentPrice > 0) {
            double factor = totalPrice / currentPrice;
            for (Product product : products) {
                product.setPrice(product.getPrice() * factor);
            }
        }
        this.totalPrice = totalPrice;
    }
    
    
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public Optional<Product> getProductById(Long productId) {
        return products.stream().filter(product -> product.getId().equals(productId)).findFirst();
    }
    
}

