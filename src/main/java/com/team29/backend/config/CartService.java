package com.team29.backend.config;
import java.util.Collections;
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
@Service
public class CartService {
    
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
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
    public Cart updateCart(Long cartId, Cart updatedCart) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(cartId);
        }
        updatedCart.setId(cartId);
        return cartRepository.save(updatedCart);
    }
    public void deleteCart(Long id) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(id);
        }
        cartRepository.deleteById(id);
    }
    public void addProduct(Long cartId, Product product) throws CartNotFoundException, ProductNotFoundException {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isEmpty()) {
            throw new CartNotFoundException(cartId);
        }
        Cart cart = optionalCart.get();
        Optional<Product> optionalProduct = productRepository.findById(product.getId());
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException(product.getId());
        }
        Product existingProduct = optionalProduct.get();
        // check if the product is already in the cart
        if (cart.getProducts().contains(existingProduct)) {
            int existingProductQuantity = Collections.frequency(cart.getProducts(), existingProduct);
            cart.setProductCount(cart.getProductCount() + product.getQuantity() - existingProductQuantity);
            cart.setTotalPrice(cart.getTotalPrice() + (existingProduct.getPrice() * (product.getQuantity() - existingProductQuantity)));
            for (int i = 0; i < product.getQuantity() - existingProductQuantity; i++) {
                cart.getProducts().add(existingProduct);
            }
        } else {
            for (int i = 0; i < product.getQuantity(); i++) {
                cart.getProducts().add(existingProduct);
            }
            cart.setProductCount(cart.getProductCount() + product.getQuantity());
            cart.setTotalPrice(cart.getTotalPrice() + (existingProduct.getPrice() * product.getQuantity()));
        }
        cartRepository.save(cart);
    }
    
    
    public void removeProduct(Long id, Long productId, int quantity) throws CartNotFoundException, ProductNotFoundException {
        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(id);
        }
        Cart existingCart = cart.get();
        List<Product> products = existingCart.getProducts();
        Optional<Product> optionalProduct = products.stream().filter(product -> product.getId().equals(productId)).findFirst();
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException(productId);
        }
        Product product = optionalProduct.get();
        int cartProductQuantity = products.stream().filter(p -> p.getId().equals(productId)).mapToInt(Product::getQuantity).sum();
        if (cartProductQuantity < quantity) {
            throw new IllegalArgumentException("Quantity to remove exceeds product quantity in cart");
        }
        for (int i = 0; i < quantity; i++) {
            products.remove(product);
        }
        existingCart.setProductCount(existingCart.getProductCount() - quantity);
        existingCart.setTotalPrice(existingCart.getTotalPrice() - (product.getPrice() * quantity));
        cartRepository.save(existingCart);
    }
    
    public double getProductPrice(Long productId) throws ProductNotFoundException {
        Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));
        return product.getPrice();
    }
    public int getProductCount(Long id) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(id);
        }
        Cart existingCart = cart.get();
        return existingCart.getProducts().stream().mapToInt(Product::getQuantity).sum();
    }
  
    public double getTotalPrice(Long id) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(id);
        }
        
        Cart existingCart = cart.get();
        return existingCart.getProducts().stream()
                        .mapToDouble(p -> p.getQuantity() * p.getPrice())
                        .sum();
    }
    public int getCartQuantity(Long id) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(id);
        }
        Cart existingCart = cart.get();
        return existingCart.getProductCount();
    }
    public void updateCartQuantity(Long id, int quantity) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(id);
        }
        Cart existingCart = cart.get();
        existingCart.setProductCount(quantity);
        cartRepository.save(existingCart);
    }
    
    public List<Product> getProductsInCart(Long id) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(id);
        }
        Cart existingCart = cart.get();
        return existingCart.getProducts();
    }
    public boolean hasProductInCart(Long id, Long productId) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(id);
        }
        Cart existingCart = cart.get();
        return existingCart.getProducts().stream()
                        .anyMatch(p -> p.getId().equals(productId));
    }
    public void removeAllProducts(Long id) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(id);
        }
        Cart existingCart = cart.get();
        existingCart.setProductCount(0);
        existingCart.setTotalPrice(0.0);
        existingCart.getProducts().clear();
        cartRepository.save(existingCart);
    }

}