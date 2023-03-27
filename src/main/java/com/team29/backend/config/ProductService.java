package com.team29.backend.config;

import com.team29.backend.exception.ProductNotFoundException;
import com.team29.backend.model.Product;
import com.team29.backend.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        } else {
            throw new ProductNotFoundException(id);
        }
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    public double calculateTotalPrice(List<Long> productIds) {
        double totalPrice = 0;
        for (Long productId : productIds) {
            // retrieve the product details by ID and calculate the total price
            Product product = getProductById(productId);
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }
}
