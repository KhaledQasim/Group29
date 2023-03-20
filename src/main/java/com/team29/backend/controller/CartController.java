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

@RestController
@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:8080"}, allowCredentials = "true")
@RequestMapping("/carts")
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
    public ResponseEntity<Cart> createCart(@RequestBody(required = true) Cart newCart) {
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
    public ResponseEntity<Cart> updateCart(@PathVariable("cartId") Long cartId, @RequestBody Cart updatedCart) {
        try {
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

    @PostMapping("/{cartId}/add-product")
    public ResponseEntity<Void> addProductToCart(@PathVariable("cartId") Long cartId, @RequestParam("productId") Long productId, @RequestParam("quantity") int quantity) {
        try {
            cartService.addProduct(cartId, productId, quantity);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (CartNotFoundException | ProductNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{cartId}/delete-product/{productId}")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable("cartId") Long cartId, @PathVariable("productId") Long productId, @RequestParam("quantity") int quantity) {
        try {
            cartService.removeProduct(cartId, productId, quantity);
            return new ResponseEntity<>(HttpStatus.OK);
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
    @GetMapping("/{cartId}/total-price")
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

    @GetMapping("/{id}/products/{productId}")
    public ResponseEntity<Boolean> hasProductInCart(@PathVariable("cartId") Long cartId, @PathVariable("productId") Long productId) {
        try {
            boolean hasProduct = cartService.hasProductInCart(cartId, productId);
            return new ResponseEntity<>(hasProduct, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/{cartId}/products/{productId}/quantity")
public ResponseEntity<Void> updateProductQuantityInCart(@PathVariable("cartId") Long cartId, @PathVariable("productId") Long productId, @RequestParam("quantity") int quantity) {
    try {
        cartService.updateProductQuantity(cartId, productId, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    } catch (CartNotFoundException|ProductNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (IllegalArgumentException e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
@GetMapping("/{cartId}/products/{productId}/quantity")
public ResponseEntity<Integer> getProductQuantityInCart(@PathVariable("cartId") Long cartId, @PathVariable("productId") Long productId) {
    try {
        int quantity = cartService.getProductQuantity(cartId, productId);
        return new ResponseEntity<>(quantity, HttpStatus.OK);
    } catch (CartNotFoundException | ProductNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
}