package com.team29.backend.exception;

public class ProductNotFoundException extends RuntimeException {

    

	public ProductNotFoundException(Long id) {
        super("Could not find the product matching the given Id: " + id);
    }
    
}
