package com.simple.api.simple_api.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handlerAccessDeniedException(AccessDeniedException ex){
        String message = "You Dont Have Permission to this Action";
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }

}
