package com.team29.backend.repository;

import com.team29.backend.model.Cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findById(Long id);

}
