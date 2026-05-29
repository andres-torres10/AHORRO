package com.ingenieriadelahorro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String tipo = "Bearer";
    private Long usuarioId;
    private String nombre;
    private String correo;
    private String fotoPerfil;
    private Boolean darkMode;

    public AuthResponse(String token, Long usuarioId, String nombre, String correo, String fotoPerfil, Boolean darkMode) {
        this.token = token;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.correo = correo;
        this.fotoPerfil = fotoPerfil;
        this.darkMode = darkMode;
    }
}
