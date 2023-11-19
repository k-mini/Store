package com.kmini.store.ex;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String exception(Exception ex) {
        log.error("msg = " + ex.getMessage(), ex);
        return "error/5xx";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String badRequest(IllegalArgumentException ex) {
        log.error("msg = " + ex.getMessage(), ex);
        return "error/400";
    }
}
