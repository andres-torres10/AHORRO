package com.ingenieriadelahorro.controller;

import com.ingenieriadelahorro.dto.ApiResponse;
import com.ingenieriadelahorro.model.Usuario;
import com.ingenieriadelahorro.security.JwtUtil;
import com.ingenieriadelahorro.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired private UsuarioService usuarioService;
    @Autowired private JwtUtil jwtUtil;

    @GetMapping("/perfil")
    public ResponseEntity<ApiResponse<Usuario>> getPerfil(@RequestHeader("Authorization") String auth) {
        Long uid = extractUserId(auth);
        Usuario u = usuarioService.getById(uid);
        u.setPassword(null); // No exponer password
        return ResponseEntity.ok(ApiResponse.ok(u));
    }

    @PutMapping("/perfil")
    public ResponseEntity<ApiResponse<Usuario>> updatePerfil(
            @RequestHeader("Authorization") String auth,
            @RequestBody Usuario datos) {
        try {
            Long uid = extractUserId(auth);
            Usuario u = usuarioService.update(uid, datos);
            u.setPassword(null);
            return ResponseEntity.ok(ApiResponse.ok("Perfil actualizado", u));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/cambiar-password")
    public ResponseEntity<ApiResponse<Void>> cambiarPassword(
            @RequestHeader("Authorization") String auth,
            @RequestBody Map<String, String> body) {
        try {
            Long uid = extractUserId(auth);
            usuarioService.cambiarPassword(uid, body.get("actual"), body.get("nueva"));
            return ResponseEntity.ok(ApiResponse.ok("Contraseña actualizada", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/foto")
    public ResponseEntity<ApiResponse<Usuario>> subirFoto(
            @RequestHeader("Authorization") String auth,
            @RequestParam("file") MultipartFile file) {
        try {
            Long uid = extractUserId(auth);
            Usuario u = usuarioService.subirFoto(uid, file);
            u.setPassword(null);
            return ResponseEntity.ok(ApiResponse.ok("Foto actualizada", u));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    private Long extractUserId(String auth) {
        return jwtUtil.extractUserId(auth.replace("Bearer ", ""));
    }
}
