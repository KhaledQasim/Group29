package com.team29.backend.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.team29.backend.controller.UserController;
import com.team29.backend.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service

@PropertySource(value = {"classpath:application.properties"})
public class JwtService {
   
   
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    // @GetMapping("/product/{id}")
    // Product getProductById(@PathVariable Long id){
    //     return productRepository.findById(id)
    //             .orElseThrow(()->new ProductNotFoundException(id));
    // }
  
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    public String generateToken(UserDetails userDetails) {   
        return generateToken(new HashMap<>(), userDetails);
    }
    
    public String generateToken(
        Map<String, Object> extraClaims,
        UserDetails userDetails
    ) {
        // 1000 * 60 * 60 * 24
      return Jwts
          .builder()
          .setClaims(extraClaims)
          .setSubject(userDetails.getUsername())
          .claim("authorities" , userDetails.getAuthorities())          
          .setIssuedAt(new Date(System.currentTimeMillis()))
          .setExpiration(new Date(System.currentTimeMillis()+1000 * 60 * 60 * 24))  //makes the token valid for only 1000mili-seconds + 24 hours
          .signWith(getSignInKey(), SignatureAlgorithm.HS256)
          .compact(); //generates and returns the token
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    
    private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
    }
    
    private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
