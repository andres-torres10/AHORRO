package com.ingenieriadelahorro.controller;

import com.ingenieriadelahorro.dto.ApiResponse;
import com.ingenieriadelahorro.model.Deuda;
import com.ingenieriadelahorro.security.JwtUtil;
import com.ingenieriadelahorro.service.DeudaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/deudas")
@CrossOrigin(origins = "*")
public class DeudaController {

    @Autowired private DeudaService deudaService;
    @Autowired private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Deuda>>> getAll(@RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(ApiResponse.ok(deudaService.getAll(extractUserId(auth))));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Deuda>> create(
            @RequestHeader("Authorization") String auth, @RequestBody Deuda deuda) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Deuda creada", deudaService.save(extractUserId(auth), deuda)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Deuda>> update(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long id, @RequestBody Deuda deuda) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Actualizada", deudaService.update(extractUserId(auth), id, deuda)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @RequestHeader("Authorization") String auth, @PathVariable Long id) {
        try {
            deudaService.delete(extractUserId(auth), id);
            return ResponseEntity.ok(ApiResponse.ok("Eliminada", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    private Long extractUserId(String auth) {
        return jwtUtil.extractUserId(auth.replace("Bearer ", ""));
    }
}
