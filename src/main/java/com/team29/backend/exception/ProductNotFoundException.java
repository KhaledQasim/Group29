package com.team29.backend.exception;

public class ProductNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1706878377314510641L;

	public ProductNotFoundException(Long id) {
        super("Could not find the product matching the given Id: " + id);
    }
    
}
