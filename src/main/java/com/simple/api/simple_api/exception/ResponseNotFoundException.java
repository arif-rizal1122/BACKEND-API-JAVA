package com.simple.api.simple_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResponseNotFoundException extends RuntimeException {
    
    public ResponseNotFoundException(String message){
        super(message);

    }

}
