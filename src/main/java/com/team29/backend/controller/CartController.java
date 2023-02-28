package com.team29.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.team29.backend.exception.CartAlreadyExistsException;
import com.team29.backend.exception.CartNotFoundException;
import com.team29.backend.model.Cart;
import com.team29.backend.model.Product;
import com.team29.backend.config.CartService;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    
    @Autowired
    private CartService cartService;

    @GetMapping
    public List<Cart> getAllCarts() {
        return cartService.getAllCarts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable("id") Long id) {
     Optional<Cart> cart = cartService.getCartById(id);
     if (cart.isPresent()) {
        return new ResponseEntity<>(cart.get(), HttpStatus.OK);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}


    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestBody Cart newCart) {
        try {
            Cart createdCart = cartService.createCart(newCart);
            return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
        } catch (CartAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cart> updateCart(@PathVariable("id") Long id, @RequestBody Cart updatedCart) {
        try {
            Cart savedCart = cartService.updateCart(id, updatedCart);
            return new ResponseEntity<>(savedCart, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable("id") Long id) {
        try {
            cartService.deleteCart(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{cartId}/products")
    public ResponseEntity<Void> addProductToCart(@PathVariable("cartId") Long cartId, @RequestBody Product product) {
        try {
            cartService.addProduct(cartId, product);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{cartId}/products/{productId}")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable("cartId") Long cartId, @PathVariable("productId") Long productId) {
        try {
            cartService.removeProduct(cartId, productId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{cartId}/products/count")
    public ResponseEntity<Integer> getProductCount(@PathVariable("cartId") Long cartId) {
        try {
            int count = cartService.getProductCount(cartId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{cartId}/total-price")
    public ResponseEntity<Double> getTotalPrice(@PathVariable("cartId") Long cartId) {
        try {
            double totalPrice = cartService.getTotalPrice(cartId);
            return new ResponseEntity<>(totalPrice, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
