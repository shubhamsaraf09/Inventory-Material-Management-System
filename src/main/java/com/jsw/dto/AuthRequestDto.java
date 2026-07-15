package com.jsw.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequestDto {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message="Password cannot be blank")
    private String password;

}
