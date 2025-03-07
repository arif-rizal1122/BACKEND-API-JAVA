package com.simple.api.simple_api.dto.request;

import lombok.Data;

@Data
public class CreateUserRequest {
    
    private String firstName;

    private String lastName;

    private String email;

    private String password;

}
