package com.team29.backend.controller;

import com.team29.backend.exception.ProductNotFoundException;
import com.team29.backend.model.Product;
import com.team29.backend.repository.ProductRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
// TO DO , place exact url of frontend server when ready to deploy
@RequestMapping
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"}, allowCredentials = "true")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    
    
    @PostMapping("/api/product")
    Product newProduct(@RequestBody Product newProduct){
        return productRepository.save(newProduct);
    }

    @GetMapping("/products")
    List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @GetMapping("/productsCategory/{category}")
    ArrayList<Product> getProductByCategory(@PathVariable String category){
        ArrayList<Product> Category = new ArrayList<>();
        for (Product temp : productRepository.findAll()) {
            if (temp.getCategory().equals(category)) {
                Category.add(temp);

            }
        }
        return Category;
                
    }

    @GetMapping("/products/new")
    ArrayList<Product> getProductNew(){
        ArrayList<Product> New = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        LocalDate currentDateMinus2Weeks = currentDate.minusDays(14);
        for (Product temp : productRepository.findAll()) {
            if (temp.getCreatedAt().isAfter(currentDateMinus2Weeks)){
                New.add(temp);
            }
        }
        return New;
                
    }

    @GetMapping("/product/{id}")
    Product getProductById(@PathVariable Long id){
        return productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException(id));
    }
    

    @PutMapping("/api/product/{id}")
    Product updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(newProduct.getName());
                    product.setPrice(newProduct.getPrice());
                    product.setImage(newProduct.getImage());
                    product.setImages(newProduct.getImages());
                    product.setDescription(newProduct.getDescription());
                    product.setCategory(newProduct.getCategory());
                    product.setSize(newProduct.getSize());
                    product.setQuantity(newProduct.getQuantity());
                    return productRepository.save(product);
                }).orElseThrow(() -> new ProductNotFoundException(id));
    }
    
  

    @DeleteMapping("/api/product/{id}")
    String deleteUser(@PathVariable Long id){
        if(!productRepository.existsById(id)){
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
        return "Product with id: "+id+" has been deleted!";
    }

    @PutMapping("/api/product/reduce")
    ResponseEntity<Object> ReduceProduct(@RequestBody ReduceProduct reduceProduct){
        ArrayList<Long> id = new ArrayList<>();
        for (int i = 0; i < reduceProduct.getId().size(); i++) {
            int x = reduceProduct.getQuantity().get(i).intValue();
            if(!productRepository.existsById(reduceProduct.getId().get(i).longValue())){
                throw new ProductNotFoundException(reduceProduct.getId().get(i).longValue());
            }
            if (productRepository.findById(reduceProduct.getId().get(i).longValue()).get().getQuantity() < x){
                return ResponseEntity.badRequest().body("Not enough quantity in stock");
            }
        }
        for(int i = 0; i < reduceProduct.getQuantity().size(); i++){
            // System.out.println(reduceProduct.getId().get(i).longValue());
            int x = reduceProduct.getQuantity().get(i).intValue();
         
        
           
            productRepository.findById(reduceProduct.getId().get(i).longValue())
                .map(product -> {
                    product.setQuantity(
                        product.getQuantity() - (x)
                    );
                    return productRepository.save(product);
                }).orElseThrow(() -> new ProductNotFoundException(reduceProduct.getId().get(0).longValue()));
            
        }


        // if(!productRepository.existsById(reduceProduct.getId())){
        //     throw new ProductNotFoundException(reduceProduct.getId());
        // }
        // if (productRepository.findById(reduceProduct.getId()).get().getQuantity() < reduceProduct.getQuantity()){
        //     return ResponseEntity.badRequest().body("Not enough quantity in stock");
        // }
        // productRepository.findById(reduceProduct.getId())
        //     .map(product -> {
        //         product.setQuantity(
        //             product.getQuantity() - reduceProduct.getQuantity()
        //         );
        //         return productRepository.save(product);
        //     }).orElseThrow(() -> new ProductNotFoundException(reduceProduct.getId()));
        // return ResponseEntity.ok().build();


        
        return ResponseEntity.ok().build();
    }

}
