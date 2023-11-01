package com.kmini.store.ex;

public class CustomUserNotFoundException extends RuntimeException {

    public CustomUserNotFoundException(String message) {
        super(message);
    }
}
