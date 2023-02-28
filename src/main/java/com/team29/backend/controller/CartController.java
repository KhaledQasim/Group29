
package com.team29.backend.controller;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.team29.backend.model.Cart;
import com.team29.backend.config.CartService;
import com.team29.backend.exception.CartNotFoundException;
import com.team29.backend.exception.CartAlreadyExistsException;
import com.team29.backend.model.Product;

@RestController
@RequestMapping("/carts")
@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:8080"}, allowCredentials = "true")
public class CartController {
    
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/carts/add")
    public ResponseEntity<Cart> createCart(@RequestBody Cart newCart) throws CartAlreadyExistsException {
        Cart cart = cartService.createCart(newCart);
        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    @GetMapping("/carts/get")
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> carts = cartService.getAllCarts();
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }

    @GetMapping("/cart/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long id) throws CartNotFoundException {
        Optional<Cart> cart = cartService.getCartById(id);
        if (!cart.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cart.get(), HttpStatus.OK);
    }

    @PutMapping("/cart/{id}")
    public ResponseEntity<Cart> updateCart(@PathVariable Long id, @RequestBody Cart updatedCart) throws CartNotFoundException {
        Cart cart = cartService.updateCart(id, updatedCart);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @DeleteMapping("/cart/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) throws CartNotFoundException {
        cartService.deleteCart(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/cart/{cartId}/addproducts")
    public ResponseEntity<Void> addProduct(@PathVariable Long cartId, @RequestBody Product productToAdd) throws CartNotFoundException {
        cartService.addProduct(cartId, productToAdd);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/cart/{cartId}/reomveproducts")
    public ResponseEntity<Void> removeProduct(@PathVariable Long cartId, @RequestBody Product productToRemove) throws CartNotFoundException {
        cartService.removeProduct(cartId, productToRemove);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/cart/{cartId}/product-count")
    public ResponseEntity<Integer> getProductCount(@PathVariable Long cartId) throws CartNotFoundException {
        int count = cartService.getProductCount(cartId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/cart/{cartId}/total-price")
    public ResponseEntity<Integer> getTotalPrice(@PathVariable Long cartId) throws CartNotFoundException {
        int totalPrice = cartService.getTotalPrice(cartId);
        return new ResponseEntity<>(totalPrice, HttpStatus.OK);
    }

}
