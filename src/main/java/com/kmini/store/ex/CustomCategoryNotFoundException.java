package com.kmini.store.ex;

public class CustomCategoryNotFoundException extends CustomException{

    public CustomCategoryNotFoundException(String message) {
        super(message);
    }

    public CustomCategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
