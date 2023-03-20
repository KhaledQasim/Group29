package com.team29.backend.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
    @GeneratedValue
    private Long id;
    
    @Column(nullable = false)
    private int quantity;
    
    private double totalPrice;
    
    @OneToMany(mappedBy = "cart")
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        boolean found = false;
        for (Product p : products) {
            if (p.getId().equals(product.getId())) {
                p.setQuantity(p.getQuantity() + product.getQuantity());
                found = true;
                break;
            }
        }
        if (!found) {
            product.setCart(this);
            products.add(product);
        }
    }

    public void removeProduct(Long productId) {
        products.removeIf(product -> product.getId().equals(productId));
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
        this.totalPrice = totalPrice * this.quantity;
    }
}
