package com.team29.backend.config;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.team29.backend.exception.CartAlreadyExistsException;
import com.team29.backend.exception.CartNotFoundException;
import com.team29.backend.model.Cart;
import com.team29.backend.model.Product;
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
        return cartRepository.save(newCart);
    }

    public Cart updateCart(Long id, Cart updatedCart) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(id);
        }
        updatedCart.setCartid(id);
        return cartRepository.save(updatedCart);
    }

    public void deleteCart(Long id) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(id);
        }
        cartRepository.deleteById(id);
    }


    public void addProduct(Long cartId, Product productToAdd) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(cartId);
        }
        Cart existingCart = cart.get();
        existingCart.getProducts().add(productToAdd);
        cartRepository.save(existingCart);
    }

    public void removeProduct(Long cartId, Long productId) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(cartId);
        }
        Cart existingCart = cart.get();
        List<Product> products = existingCart.getProducts();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(productId)) {
                products.remove(i);
                break;
            }
        }
        cartRepository.save(existingCart);
    

    }
    public int getProductCount(Long cartId) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(cartId);
        }
        Cart existingCart = cart.get();
        return existingCart.getProducts().size();
    }

    public double getTotalPrice(Long cartId) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(cartId);
        }
        Cart existingCart = cart.get();
        return existingCart.getTotalPrice();
    }
}
