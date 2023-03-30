package com.team29.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team29.backend.model.Order;
import com.team29.backend.model.OrderNew;

public interface OrderNewRepository extends JpaRepository<OrderNew,Long > {
    
    
}
