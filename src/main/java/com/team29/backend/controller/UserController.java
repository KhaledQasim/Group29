package com.team29.backend.controller;


import com.team29.backend.repository.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import com.team29.backend.config.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.team29.backend.ip.RequestService;
import com.team29.backend.auth.RegisterRequest;
import com.team29.backend.exception.UserEmailWrongException;
import com.team29.backend.exception.UserNotFoundE;
import com.team29.backend.exception.UserRegistrationDetailsMissingException;
import com.team29.backend.exception.UsernameTakenException;
import com.team29.backend.exception.WrongPassE;
import com.team29.backend.model.Role;
import com.team29.backend.model.User;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequiredArgsConstructor
@RestController
// @RequestMapping("/api/user")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"}, allowCredentials = "true")
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwt;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private RequestService requestService;
    private Role role;


    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    @GetMapping("/api/user/get")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @GetMapping("/api/user/number/get")
    Integer getAllUsersAmount() {
        return userRepository.findAll().size();
    }
    
    @GetMapping("/api/user/new")
    Integer getUserNew(){
        ArrayList<User> New = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        LocalDate currentDateMinus1Month = currentDate.minusMonths(1);
        for (User temp : userRepository.findAll()) {
            if (temp.getCreatedAt().isAfter(currentDateMinus1Month)){
                New.add(temp);
            }
        }
        return New.size();
                
    }
  
    @PostMapping("/api/user/post")
    public void register( HttpServletRequest requestIp, @RequestBody RegisterRequest request) {
        Optional<User> EmailTaken = userRepository.findByEmail(request.getEmail());
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if ((StringUtils.isBlank(request.getFirstname())) || (StringUtils.isBlank(request.getLastname()))
                || (StringUtils.isBlank(request.getPassword())) || (StringUtils.isBlank(request.getEmail()))) {

            throw new UserRegistrationDetailsMissingException();
        }

        if (EmailTaken.isPresent()) {
            throw new UsernameTakenException();
        }

        if (request.getPassword().length() <= 7) {
            throw new WrongPassE();
        }
        if (!patternMatches(request.getEmail(), regexPattern)) {
            throw new UserEmailWrongException();
        }
       
            if(request.getRole().equals("ADMIN")){
                role = Role.ADMIN;
            }
            else {
                role = Role.USER;
            }
            var user = User.builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(role)
                    .ip(requestService.getClientIp(requestIp))
                    .build();
            userRepository.save(user);
            // String token = jwt.generateToken(user)
            // return ResponseEntity.ok()
            // .header(
            //         HttpHeaders.AUTHORIZATION,
            //         jwt.generateToken(user)
            // )
            // .body(token);
           
     
    }

    @PutMapping("/api/user/put/{id}")
    User updateUser(@RequestBody User newUser, @PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstname(newUser.getFirstname());
                    user.setLastname(newUser.getLastname());
                    user.setRole(newUser.getRole());
                    return userRepository.save(user);
                }).orElseThrow(() -> new UserNotFoundE("User not found!"));
    }

    @GetMapping("/api/user/get/{id}")
    User getUserByID(@PathVariable Long id){
        return userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundE("User not found!"));
    }


    
    @DeleteMapping("/api/user/delete/{id}")
    String deleteUser(@PathVariable Long id){
        if(!userRepository.existsById(id)){
            throw new UserNotFoundE("User not found!");
        }
        userRepository.deleteById(id);
        return "User with id: "+id+" has been deleted!";
    }
    
    


    @GetMapping("/singleUser/getByUsername/{username}")
    User getUserByUsername(@PathVariable String username, @CookieValue(name = "jwt") String token) {
        if (!(userRepository.findByEmail(username)).isPresent()) {
            throw new UserNotFoundE("User not found!");
        }
        if ((Objects.equals(jwt.extractUsername(token), username))) {
            return userRepository.findByEmail(username)
                    .orElseThrow(() -> new UserNotFoundE("User not found!"));
        }
        else {
            throw new UserNotFoundE("You are unauthorized to view this users account information!");
        }
        
    }
    
    @PutMapping("/singleUser/editUser/{username}")
    public ResponseEntity<?> validateUpdateUserNames(@RequestBody User newUser, @PathVariable String username,
            @CookieValue(name = "jwt") String token, @AuthenticationPrincipal User userPrincipal) {
        try {
            Boolean isValidToken = jwt.isTokenValid(token, userPrincipal);
            if (!(Objects.equals(jwt.extractUsername(token), username))) {
                return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
            }
            userRepository.findByEmail(username)
                    .map(user -> {
                        user.setFirstname(newUser.getFirstname());
                        user.setLastname(newUser.getLastname());
                        return userRepository.save(user);
                    }).orElseThrow(() -> new UserNotFoundE("User not found!"));
            return ResponseEntity.ok(isValidToken);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.ok(false);
        }

    }

    @PutMapping("/singleUser/editUserPassword/{username}")
    public ResponseEntity<?> validateUpdateUserPassword(@RequestBody User newUser, @PathVariable String username,@CookieValue(name = "jwt") String token, @AuthenticationPrincipal User userPrincipal) {
        try {
              
            String regexPattern = "(?=\\S+$).{8,}";
            if (!patternMatches(newUser.getPassword(), regexPattern)) {
                throw new WrongPassE();
            }
            Boolean isValidToken = jwt.isTokenValid(token, userPrincipal);
            if(!(Objects.equals(jwt.extractUsername(token), username)))
            {
                return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
            }
            userRepository.findByEmail(username)
                    .map(user -> {
                        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
                       return userRepository.save(user);
                    }).orElseThrow(() -> new UserNotFoundE("User not found!"));
        
            return ResponseEntity.ok(isValidToken);
        }
        catch (ExpiredJwtException e) {
            return ResponseEntity.ok(false);
        }
   
       
    }



    // @PutMapping("/api/product/{id}")
    // Product updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
    //     return productRepository.findById(id)
    //             .map(product -> {
    //                 product.setName(newProduct.getName());
    //                 product.setPrice(newProduct.getPrice());
    //                 product.setImage(newProduct.getImage());
    //                 product.setImages(newProduct.getImages());
    //                 product.setDescription(newProduct.getDescription());
    //                 product.setCategory(newProduct.getCategory());
    //                 product.setSize(newProduct.getSize());
    //                 product.setQuantity(newProduct.getQuantity());
    //                 return productRepository.save(product);
    //             }).orElseThrow(() -> new ProductNotFoundException(id));
    // }
    

    // @GetMapping("/validate")
    // public ResponseEntity<?> validateToken(@CookieValue(name = "jwt") String token,
    //         @AuthenticationPrincipal User user) {
    //     try {
    //         Boolean isValidToken = jwt.isTokenValid(token, user);
    //         return ResponseEntity.ok(isValidToken);
    //     } catch (ExpiredJwtException e) {
    //         return ResponseEntity.ok(false);
    //     }
    // }
}
