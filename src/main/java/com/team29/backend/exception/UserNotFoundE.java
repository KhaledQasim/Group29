package com.team29.backend.exception;

public class UserNotFoundE extends RuntimeException {
    public UserNotFoundE(String msg) {
        super(msg);
    }
}
