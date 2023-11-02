package com.kmini.store.ex;

public class CustomCategoryNotFoundException extends RuntimeException{

    public CustomCategoryNotFoundException(String message) {
        super(message);
    }

    public CustomCategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
