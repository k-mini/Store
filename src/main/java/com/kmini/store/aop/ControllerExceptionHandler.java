package com.kmini.store.aop;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {


    @ExceptionHandler(IllegalArgumentException.class)
    public String badRequest() {
        return "error/400";
    }
}
