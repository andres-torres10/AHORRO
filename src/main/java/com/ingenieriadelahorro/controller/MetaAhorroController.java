package com.ingenieriadelahorro.controller;

import com.ingenieriadelahorro.dto.ApiResponse;
import com.ingenieriadelahorro.model.MetaAhorro;
import com.ingenieriadelahorro.security.JwtUtil;
import com.ingenieriadelahorro.service.MetaAhorroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/metas")
@CrossOrigin(origins = "*")
public class MetaAhorroController {

    @Autowired private MetaAhorroService metaAhorroService;
    @Autowired private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MetaAhorro>>> getAll(@RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(ApiResponse.ok(metaAhorroService.getAll(extractUserId(auth))));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MetaAhorro>> create(
            @RequestHeader("Authorization") String auth, @RequestBody MetaAhorro meta) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Meta creada", metaAhorroService.save(extractUserId(auth), meta)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MetaAhorro>> update(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long id, @RequestBody MetaAhorro meta) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Actualizada", metaAhorroService.update(extractUserId(auth), id, meta)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @RequestHeader("Authorization") String auth, @PathVariable Long id) {
        try {
            metaAhorroService.delete(extractUserId(auth), id);
            return ResponseEntity.ok(ApiResponse.ok("Eliminada", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    private Long extractUserId(String auth) {
        return jwtUtil.extractUserId(auth.replace("Bearer ", ""));
    }
}
