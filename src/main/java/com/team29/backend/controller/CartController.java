package com.team29.backend.controller;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.team29.backend.exception.CartAlreadyExistsException;
import com.team29.backend.exception.CartNotFoundException;
import com.team29.backend.exception.ProductNotFoundException;
import com.team29.backend.model.Cart;
import com.team29.backend.config.CartService;
import com.team29.backend.model.Product;
import com.team29.backend.model.Size;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:8080"}, allowCredentials = "true")
@RequestMapping("/carts")
@AllArgsConstructor
@NoArgsConstructor
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> carts = cartService.getAllCarts();

        if (!carts.isEmpty()) {
            return new ResponseEntity<>(carts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCartById(@PathVariable("cartId") Long cartId) {
        Optional<Cart> cart = cartService.getCartById(cartId);
        if (cart.isPresent()) {
            return new ResponseEntity<>(cart.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    // public ResponseEntity<Cart> createCart(@RequestBody(required = true) Cart newCart) {
    public ResponseEntity<Cart> createCart(@RequestBody Cart newCart) {
        if (newCart == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Cart createdCart = cartService.createCart(newCart);
            return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
        } catch (CartAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    @PutMapping("/{cartId}")
    public ResponseEntity<Cart> updateCart(@PathVariable("cartId") Long cartId, @RequestBody Cart cartData, 
                                            @RequestParam("quantity") int quantity, @RequestParam("totalPrice") double totalPrice) {
        try {
            Cart updatedCart = cartService.getCartById(cartId).orElseThrow(() -> new CartNotFoundException(cartId));
            updatedCart.setProducts(cartData.getProducts());
            updatedCart.setQuantity(quantity);
            updatedCart.setTotalPrice(totalPrice);
            Cart savedCart = cartService.updateCart(cartId, updatedCart);
            return new ResponseEntity<>(savedCart, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("cartId") Long cartId) {
        try {
            cartService.deleteCart(cartId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{cartId}/addproduct")
    public ResponseEntity<Void> addProductToCart(@PathVariable("cartId") Long cartId, @RequestBody Product product) {
        try {
            cartService.addProduct(cartId, product);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (CartNotFoundException | ProductNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    

    
    @DeleteMapping("/{cartId}/deleteproduct/{productId}")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable("cartId") Long cartId, @PathVariable("productId") Long productId, @RequestBody int quantity) {
try {
cartService.removeProduct(cartId, productId, quantity);
return new ResponseEntity<>(HttpStatus.NO_CONTENT);
} catch (CartNotFoundException | ProductNotFoundException e) {
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
    @GetMapping("/{cartId}/totalprice")
    public ResponseEntity<Double> getTotalPrice(@PathVariable("cartId") Long cartId) {
        try {
            double totalPrice = cartService.getTotalPrice(cartId);
            return new ResponseEntity<>(totalPrice, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{cartId}/products")
    public ResponseEntity<List<Product>> getProductsInCart(@PathVariable("cartId") Long cartId) {
        try {
            List<Product> products = cartService.getProductsInCart(cartId);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{cartId}/products/{productId}")
    public ResponseEntity<Boolean> hasProductInCart(@PathVariable("cartId") Long cartId, @PathVariable("productId") Long productId) {
        try {
            boolean hasProduct = cartService.hasProductInCart(cartId, productId);
            return new ResponseEntity<>(hasProduct, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
@DeleteMapping("/{cartId}/products")
public ResponseEntity<Void> emptyCart(@PathVariable("cartId") Long cartId) {
    try {
        cartService.removeAllProducts(cartId);
        return new ResponseEntity<>(HttpStatus.OK);
    } catch (CartNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

@GetMapping("/{cartId}/quantity")
public ResponseEntity<Integer> getCartQuantity(@PathVariable("cartId") Long cartId) {
    try {
        int quantity = cartService.getCartQuantity(cartId);
        return ResponseEntity.ok(quantity);
    } catch (CartNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

@PutMapping("/{cartId}/quantity")
public ResponseEntity<Integer> updateCartQuantity(@PathVariable("cartId") Long cartId, @RequestBody int quantity) {
    try {
        cartService.updateCartQuantity(cartId, quantity);
        return ResponseEntity.ok().build();
    } catch (CartNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
}