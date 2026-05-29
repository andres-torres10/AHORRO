package com.ingenieriadelahorro.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.ingenieriadelahorro.dto.AuthResponse;
import com.ingenieriadelahorro.model.Usuario;
import com.ingenieriadelahorro.repository.UsuarioRepository;
import com.ingenieriadelahorro.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class GoogleAuthService {

    @Value("${google.client-id:}")
    private String googleClientId;

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;

    public AuthResponse loginConGoogle(String idToken) throws Exception {
        if (googleClientId == null || googleClientId.isBlank()) {
            throw new RuntimeException("Google Client ID no configurado en application.properties");
        }

        // Verificar el token con Google
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken googleIdToken = verifier.verify(idToken);
        if (googleIdToken == null) {
            throw new RuntimeException("Token de Google inválido");
        }

        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        String email   = payload.getEmail();
        String nombre  = (String) payload.get("name");
        String foto    = (String) payload.get("picture");

        // Buscar o crear usuario
        Usuario usuario = usuarioRepository.findByCorreo(email).orElseGet(() -> {
            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre != null ? nombre : email.split("@")[0]);
            nuevo.setCorreo(email);
            // Contraseña aleatoria (no se usará para login con Google)
            nuevo.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            nuevo.setFotoPerfil(foto);
            return usuarioRepository.save(nuevo);
        });

        // Actualizar foto si cambió
        if (foto != null && !foto.equals(usuario.getFotoPerfil())) {
            usuario.setFotoPerfil(foto);
            usuarioRepository.save(usuario);
        }

        String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getId());
        return new AuthResponse(token, usuario.getId(), usuario.getNombre(),
                usuario.getCorreo(), usuario.getFotoPerfil(), usuario.getDarkMode());
    }
}
