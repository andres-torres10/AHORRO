package com.ingenieriadelahorro.controller;

import com.ingenieriadelahorro.dto.*;
import com.ingenieriadelahorro.service.AuthService;
import com.ingenieriadelahorro.service.GoogleAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired private AuthService authService;
    @Autowired private GoogleAuthService googleAuthService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(ApiResponse.ok("Registro exitoso", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.ok("Login exitoso", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Credenciales incorrectas"));
        }
    }

    @PostMapping("/google")
    public ResponseEntity<ApiResponse<AuthResponse>> loginGoogle(@RequestBody GoogleAuthRequest request) {
        try {
            AuthResponse response = googleAuthService.loginConGoogle(request.getToken());
            return ResponseEntity.ok(ApiResponse.ok("Login con Google exitoso", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
