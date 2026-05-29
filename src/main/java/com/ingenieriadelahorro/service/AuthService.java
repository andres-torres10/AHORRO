package com.ingenieriadelahorro.service;

import com.ingenieriadelahorro.dto.AuthRequest;
import com.ingenieriadelahorro.dto.AuthResponse;
import com.ingenieriadelahorro.dto.RegisterRequest;
import com.ingenieriadelahorro.model.Usuario;
import com.ingenieriadelahorro.repository.UsuarioRepository;
import com.ingenieriadelahorro.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        if (!request.getPassword().equals(request.getConfirmarPassword())) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setCorreo(request.getCorreo());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario = usuarioRepository.save(usuario);

        String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getId());
        return new AuthResponse(token, usuario.getId(), usuario.getNombre(),
                usuario.getCorreo(), usuario.getFotoPerfil(), usuario.getDarkMode());
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPassword())
        );
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getId());
        return new AuthResponse(token, usuario.getId(), usuario.getNombre(),
                usuario.getCorreo(), usuario.getFotoPerfil(), usuario.getDarkMode());
    }
}
