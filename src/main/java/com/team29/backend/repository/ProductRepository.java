package com.team29.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.team29.backend.model.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {
    
}
