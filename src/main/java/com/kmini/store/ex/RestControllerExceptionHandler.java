package com.kmini.store.ex;

import com.kmini.store.dto.CommonRespDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.kmini.store.controller.api")
@Slf4j
public class RestControllerExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgumentException(IllegalArgumentException ex) {
        log.warn("msg = " + ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonRespDto<>(-1, ex.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exception(Exception ex) {
        log.warn("msg = " + ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonRespDto<>(-1, ex.getMessage(),null));
    }
}
