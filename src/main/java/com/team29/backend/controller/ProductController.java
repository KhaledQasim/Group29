package com.team29.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.team29.backend.exception.ProductNotFoundException;
import com.team29.backend.model.Product;
import com.team29.backend.repository.ProductRepository;

@RestController
// TO DO , place exact url of frontend server when ready to deploy
@CrossOrigin("*")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    
    
    @PostMapping("/product")
    Product newProduct(@RequestBody Product newProduct){
        return productRepository.save(newProduct);
    }

    @GetMapping("/products")
    List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @GetMapping("/product/{id}")
    Product geProductById(@PathVariable Long id){
        return productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException(id));
    }

    @PutMapping("/product/{id}")
    Product updateUser(@RequestBody Product newProduct,@PathVariable Long id){
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(newProduct.getName());
                    product.setPrice(newProduct.getPrice());
                    product.setImage(newProduct.getImage());
                    return productRepository.save(product);
                }).orElseThrow(()->new ProductNotFoundException(id));
    }

    @DeleteMapping("/product/{id}")
    String deleteUser(@PathVariable Long id){
        if(!productRepository.existsById(id)){
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
        return "Product with id: "+id+" has been deleted!";
    }
}
