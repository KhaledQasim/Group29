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
import com.team29.backend.config.CartService;
import com.team29.backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@CrossOrigin(origins = {"http://localhost:3000/", "http://localhost:8080"}, allowCredentials = "true")
@RequestMapping("/carts")
public class CartController {
    
    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<Page<Cart>> getAllCarts(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Cart> carts = cartService.getAllCarts(paging);
    
        if (carts.hasContent()) {
            return new ResponseEntity<>(carts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
    public ResponseEntity<Void> addProductToCart(@PathVariable("cartId") Long cartId, @RequestParam("productId") Long productId) {
        try {
            cartService.addProduct(cartId, productId);
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
    }@GetMapping("/{cartId}/products/count")
    public ResponseEntity<Integer> getProductCount(@PathVariable("cartId") Long cartId) {
        try {
            int count = cartService.getProductCount(cartId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}/total-price")
    public ResponseEntity<Double> getTotalPrice(@PathVariable("id") Long id) {
        try {
            double totalPrice = cartService.getTotalPrice(id);
            return new ResponseEntity<>(totalPrice, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<Product>> getProductsInCart(@PathVariable("id") Long id) {
        try {
            List<Product> products = cartService.getProductsInCart(id);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/products/{productId}")
    public ResponseEntity<Boolean> hasProductInCart(@PathVariable("id") Long id, @PathVariable("productId") Long productId) {
        try {
            boolean hasProduct = cartService.hasProductInCart(id, productId);
            return new ResponseEntity<>(hasProduct, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

  

}