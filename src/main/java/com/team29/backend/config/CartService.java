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
    public void addProduct(Long cartId, Long productId, int quantity) throws CartNotFoundException, ProductNotFoundException {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isEmpty()) {
            throw new CartNotFoundException(cartId);
        }
        Cart cart = optionalCart.get();
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException(productId);
        }
        Product product = optionalProduct.get();
        // set the product's cart to the current cart
        product.setCart(cart);
        // add the product (with the given quantity) to the cart's list of products
        for (int i = 0; i < quantity; i++) {
            cart.getProducts().add(product);
        }
        cart.setProductCount(cart.getProductCount() + quantity);
        cart.setTotalPrice(cart.getTotalPrice() + (product.getPrice() * quantity));
        cartRepository.save(cart);
    }
    public void removeProduct(Long cartId, Long productId, int quantity) throws CartNotFoundException, ProductNotFoundException {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(cartId);
        }
        Cart existingCart = cart.get();
        List<Product> products = existingCart.getProducts();
        Optional<Product> optionalProduct = products.stream().filter(product -> product.getId().equals(productId)).findFirst();
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException(productId);
        }
        Product product = optionalProduct.get();
        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Quantity to remove exceeds product quantity in cart");
        }
        if (product.getQuantity() < quantity) {
            products.remove(product);
        } else {
            product.setQuantity(product.getQuantity() - quantity);
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
    public int getProductCount(Long cartId) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(cartId);
        }
        Cart existingCart = cart.get();
        return existingCart.getProducts().stream().mapToInt(Product::getQuantity).sum();
    }
    public int getProductQuantity(Long cartId, Long productId) throws CartNotFoundException, ProductNotFoundException {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isEmpty()) {
            throw new CartNotFoundException(cartId);
        }
        Cart cart = optionalCart.get();
        Optional<Product> optionalProduct = cart.getProducts().stream()
                                              .filter(p -> p.getId().equals(productId))
                                              .findFirst();
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException(productId);
        }
        return optionalProduct.get().getQuantity();
    }
    public double getTotalPrice(Long cartId) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(cartId);
        }
        Cart existingCart = cart.get();
        return existingCart.getProducts().stream()
                        .mapToDouble(p -> p.getQuantity() * p.getPrice())
                        .sum();
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
        return existingCart.getProducts().stream()
                        .anyMatch(p -> p.getId().equals(productId));
    }
    public void updateProductQuantity(Long cartId, Long productId, int quantity) throws CartNotFoundException, ProductNotFoundException {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isEmpty()) {
            throw new CartNotFoundException(cartId);
        }
        Cart cart = optionalCart.get();
        Optional<Product> optionalProduct = cart.getProducts().stream()
                                              .filter(p -> p.getId().equals(productId))
                                              .findFirst();
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException(productId);
        }
        Product product = optionalProduct.get();
        int oldQuantity = product.getQuantity();
        product.setQuantity(quantity);
        cart.setProductCount(cart.getProductCount() + quantity - oldQuantity);
        cart.setTotalPrice(cart.getTotalPrice() + (quantity - oldQuantity) * product.getPrice());
        cartRepository.save(cart);
    }
    public void removeAllProducts(Long cartId) throws CartNotFoundException {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new CartNotFoundException(cartId);
        }
        Cart existingCart = cart.get();
        existingCart.getProducts().clear();
        existingCart.setProductCount(0);
        existingCart.setTotalPrice(0.0);
        cartRepository.save(existingCart);
    }
}
