package com.team29.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team29.backend.model.Order;

public interface OrderRepository extends JpaRepository<Order,Integer > {
    
    
}
