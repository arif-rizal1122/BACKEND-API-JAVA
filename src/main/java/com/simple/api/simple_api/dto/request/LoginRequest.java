package com.simple.api.simple_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    
    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
