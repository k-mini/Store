package com.kmini.store.ex;

public class CustomCommentNotFoundException extends RuntimeException {

    public CustomCommentNotFoundException(String message) {
        super(message);
    }

    public CustomCommentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
