package com.simple.api.simple_api.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyExistException extends RuntimeException {
    
    public AlreadyExistException(String message){
        super(message);
    }

}
