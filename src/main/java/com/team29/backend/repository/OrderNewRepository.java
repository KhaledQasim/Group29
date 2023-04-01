package com.team29.backend.repository;

import com.team29.backend.model.OrderNew;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;



public interface OrderNewRepository extends JpaRepository<OrderNew,Long > {
    Optional<OrderNew> findByEmail(String email);
    
}
