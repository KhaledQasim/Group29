package com.team29.backend.config;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.team29.backend.exception.ProductNotFoundException;
import com.team29.backend.exception.CartAlreadyExistsException;
import com.team29.backend.exception.CartNotFoundException;
import com.team29.backend.model.Cart;
import com.team29.backend.model.Product;
import com.team29.backend.repository.CartRepository;
import com.team29.backend.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
@Service
public class CartService {
    
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public Page<Cart> getAllCarts(Pageable pageable) {
        return cartRepository.findAll(pageable);
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

    public void addProduct(Long cartId, Long productId) throws CartNotFoundException, ProductNotFoundException {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(cartId);
        }
        Cart existingCart = cart.get();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        existingCart.getProducts().add(product);
        existingCart.setProductCount(existingCart.getProductCount() + 1);
        existingCart.setTotalPrice(existingCart.getTotalPrice() + product.getPrice());
        cartRepository.save(existingCart);
    }
    private double getProductPrice(Long productId) throws ProductNotFoundException {
        Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));
        return product.getPrice();
    }

    public void removeProduct(Long cartId, Long productId) throws CartNotFoundException, ProductNotFoundException {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(cartId);
        }
        Cart existingCart = cart.get();
        List<Product> products = existingCart.getProducts();
        boolean productRemoved = products.removeIf(product -> product.getId().equals(productId));
        if (!productRemoved) {
            throw new ProductNotFoundException(productId);
        }
        existingCart.setProductCount(existingCart.getProductCount() - 1);
        existingCart.setTotalPrice(existingCart.getTotalPrice() - getProductPrice(productId));
        cartRepository.save(existingCart);
    }

    public int getProductCount(Long cartId) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(cartId);
        }
        Cart existingCart = cart.get();
        return existingCart.getProductCount();
    }

    public double getTotalPrice(Long cartId) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(cartId);
        }
        Cart existingCart = cart.get();
        return existingCart.getTotalPrice();
    }

    public List<Product> getProductsInCart(Long cartId) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(cartId);
        }
        Cart existingCart = cart.get();
        return existingCart.getProducts();
    }
    public boolean hasProductInCart(Long cartId, Long productId) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(cartId);
        }
        Cart existingCart = cart.get();
        List<Product> products = existingCart.getProducts();
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return true;
            }
        }
        return false;
    }
}