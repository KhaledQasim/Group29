package com.team29.backend.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Temporal;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.ManyToMany;

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
    private Long cartid;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    private int quantity;

    private double totalPrice;

    @ManyToMany
    private List<Product> products;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

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
            products.add(product);
        }
    }

    public void removeProduct(Long productId) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(productId)) {
                products.remove(i);
                break;
            }
        }
    }

    public int getProductCount() {
        int count = 0;
        for (Product product : products) {
            count += product.getQuantity();
        }
        return count * quantity;
    }

    public double getPrice() {
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
                product.setQuantity((long) (product.getQuantity() * factor));
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
