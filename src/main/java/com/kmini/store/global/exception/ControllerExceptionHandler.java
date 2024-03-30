package com.kmini.store.global.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String exception(Exception ex) {
        log.warn("msg = " + ex.getMessage(), ex);
        return "error/5xx";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String badRequest(IllegalArgumentException ex) {
        log.warn("msg = " + ex.getMessage(), ex);
        return "error/400";
    }
}
