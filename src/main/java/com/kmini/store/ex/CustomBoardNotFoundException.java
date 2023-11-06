package com.kmini.store.ex;

public class CustomBoardNotFoundException extends CustomException {

    public CustomBoardNotFoundException(String message) {
        super(message);
    }

    public CustomBoardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
