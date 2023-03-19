package com.team29.backend.exception;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(Long id){
        super("Cart not found with ID: " + id);
}
}