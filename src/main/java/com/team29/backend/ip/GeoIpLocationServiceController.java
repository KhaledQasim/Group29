package com.team29.backend.ip;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import com.team29.backend.repository.ProductRepository;
import com.team29.backend.repository.UserRepository;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import com.team29.backend.model.Product;
import com.team29.backend.model.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/GeoIp")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"}, allowCredentials = "true")
public class GeoIpLocationServiceController {
   

    private final UserRepository userRepository;
    @GetMapping("/get")
    public <T> Map<String, Integer> getLocation()  {
             
       
        try {
            Map<T, Long> resultMap = new HashMap<>();
            ArrayList<String> Ips = new ArrayList<>();
            ArrayList<User> User = new ArrayList<>();
            File database = new File("src/main/java/com/team29/backend/ip/GeoLite2-Country.mmdb");
            
            DatabaseReader reader = new DatabaseReader.Builder(database).build();
            
           

            for (User temp : userRepository.findAll()) {
                if (!(temp.getIp().equals("127.0.0.1"))) {
                    String ip = temp.getIp();
                    InetAddress ipAddress = InetAddress.getByName(ip);

                    CountryResponse response = reader.country(ipAddress);

                    Country country = response.getCountry();
                    String alpha3Country = new Locale("en", country.getIsoCode()).getISO3Country();
                    Ips.add(alpha3Country);

                }

            }
            Map<String, Integer> duplicates = new HashMap<String, Integer>(); 
            for (String str : Ips) {
                if (duplicates.containsKey(str)) {
                    duplicates.put(str, duplicates.get(str) + 1);
                } else {
                    duplicates.put(str, 1);
                }
            }
            
   
     
            return duplicates; 
        }
        catch (IOException | java.io.IOException | GeoIp2Exception e) {
            Map<String, Integer> duplicates = new HashMap<String, Integer>(); 
            
            duplicates.put(e.getMessage(),0 );
            return duplicates;
        }
   
      
        
    }
}
