package com.ingenieriadelahorro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
    @Email
    @NotBlank
    private String correo;

    @NotBlank
    private String password;
}
