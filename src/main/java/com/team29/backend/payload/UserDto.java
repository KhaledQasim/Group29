package com.team29.backend.payload;

import java.time.Instant;

import com.team29.backend.model.Role;

import lombok.Getter;
import lombok.Setter;

public class UserDto {
    @Getter
    @Setter
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String ip;

    
    private Instant createdAt;
   

   
    private Role role;
    
    
    


}
