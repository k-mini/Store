package com.kmini.store.aop;

import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.ex.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> apiException(CustomException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonRespDto<>(-1,e.getMessage(),null));
    }
}
