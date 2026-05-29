package com.ingenieriadelahorro.controller;

import com.ingenieriadelahorro.dto.ApiResponse;
import com.ingenieriadelahorro.model.Presupuesto;
import com.ingenieriadelahorro.security.JwtUtil;
import com.ingenieriadelahorro.service.PresupuestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/presupuestos")
@CrossOrigin(origins = "*")
public class PresupuestoController {

    @Autowired private PresupuestoService presupuestoService;
    @Autowired private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Presupuesto>>> getAll(
            @RequestHeader("Authorization") String auth,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer anio) {
        Long uid = extractUserId(auth);
        List<Presupuesto> list = (mes != null && anio != null)
                ? presupuestoService.getByMes(uid, mes, anio)
                : presupuestoService.getAll(uid);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Presupuesto>> create(
            @RequestHeader("Authorization") String auth, @RequestBody Presupuesto p) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Creado", presupuestoService.save(extractUserId(auth), p)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Presupuesto>> update(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long id, @RequestBody Presupuesto p) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Actualizado", presupuestoService.update(extractUserId(auth), id, p)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @RequestHeader("Authorization") String auth, @PathVariable Long id) {
        try {
            presupuestoService.delete(extractUserId(auth), id);
            return ResponseEntity.ok(ApiResponse.ok("Eliminado", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    private Long extractUserId(String auth) {
        return jwtUtil.extractUserId(auth.replace("Bearer ", ""));
    }
}
