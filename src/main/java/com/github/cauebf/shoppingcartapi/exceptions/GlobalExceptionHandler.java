package com.github.cauebf.shoppingcartapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // listens for exceptions in the entire application
public class GlobalExceptionHandler {
    
    // this method will be called when an AccessDeniedException is thrown
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        // return a custom error response
        String message = "You don't have permission to this resource: " + e.getMessage();
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }
}
