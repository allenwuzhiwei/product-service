package com.nusiss.productservice.config;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}