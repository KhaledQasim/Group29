package com.team29.backend.config;

import java.util.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.team29.backend.model.Cart;
import com.team29.backend.exception.CartNotFoundException;
import com.team29.backend.exception.CartAlreadyExistsException;
import com.team29.backend.repository.CartRepository;

@Service
public class CartService {
    
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public Optional<Cart> getCartById(Long id) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(id);
        }
        return cart;
    }

    public Cart createCart(Cart newCart) throws CartAlreadyExistsException {
        if (cartRepository.existsByName(newCart.getName())) {
            throw new CartAlreadyExistsException("Cart already exists with name: " + newCart.getName());
        }
        return cartRepository.save(newCart);
    }

    public Cart updateCart(Long id, Cart updatedCart) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(id);
        }
        updatedCart.setId(id);
        return cartRepository.save(updatedCart);
    }

    public void deleteCart(Long id) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(id);
        }
        cartRepository.deleteById(id);
    }
}
