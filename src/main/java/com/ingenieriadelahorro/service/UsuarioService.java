package com.ingenieriadelahorro.service;

import com.ingenieriadelahorro.model.Usuario;
import com.ingenieriadelahorro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;

@Service
public class UsuarioService {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public Usuario getById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario update(Long id, Usuario datos) {
        Usuario u = getById(id);
        u.setNombre(datos.getNombre());
        if (datos.getDarkMode() != null) u.setDarkMode(datos.getDarkMode());
        return usuarioRepository.save(u);
    }

    public Usuario cambiarPassword(Long id, String actual, String nueva) {
        Usuario u = getById(id);
        if (!passwordEncoder.matches(actual, u.getPassword())) {
            throw new RuntimeException("Contraseña actual incorrecta");
        }
        u.setPassword(passwordEncoder.encode(nueva));
        return usuarioRepository.save(u);
    }

    public Usuario subirFoto(Long id, MultipartFile file) throws IOException {
        Usuario u = getById(id);
        String uploadDir = "uploads/fotos/";
        Files.createDirectories(Paths.get(uploadDir));
        String filename = "user_" + id + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir + filename);
        Files.write(path, file.getBytes());
        u.setFotoPerfil("/uploads/fotos/" + filename);
        return usuarioRepository.save(u);
    }
}
