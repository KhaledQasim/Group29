package com.team29.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.team29.backend.model.Product;
//Repository is used to connect to the DB using an ID

public interface ProductRepository extends JpaRepository<Product,Long> {
    
}
