package com.team29.backend.exception;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(String string){
        super("Cart not found with ID: " + string);
}
}