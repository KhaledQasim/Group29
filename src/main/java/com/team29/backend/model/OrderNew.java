package com.team29.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderNew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @CreationTimestamp
    private LocalDate createdAt;
    private String paymentType;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String city;
    private String postCode;
    @Column(name="products", length=512)
    private String products;
    private Long userId;
    private Integer totalPrice;
    // @ManyToOne
    // @JoinColumn(name = "user_id",nullable = false)
    // private User user;
}