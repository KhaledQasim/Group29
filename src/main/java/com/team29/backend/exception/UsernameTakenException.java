package com.team29.backend.exception;




public class UsernameTakenException extends RuntimeException {

    

	public UsernameTakenException() {
        super("This Email is already taken!");
    }
    
}
