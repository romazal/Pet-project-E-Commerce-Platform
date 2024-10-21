package com.romazal.ecommerce.handler;

import com.romazal.ecommerce.exception.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handle(CustomerNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

}
