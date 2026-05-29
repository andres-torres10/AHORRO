package com.ingenieriadelahorro.controller;

import com.ingenieriadelahorro.dto.ApiResponse;
import com.ingenieriadelahorro.model.Ingreso;
import com.ingenieriadelahorro.security.JwtUtil;
import com.ingenieriadelahorro.service.IngresoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ingresos")
@CrossOrigin(origins = "*")
public class IngresoController {

    @Autowired private IngresoService ingresoService;
    @Autowired private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Ingreso>>> getAll(
            @RequestHeader("Authorization") String auth,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer anio) {
        Long uid = extractUserId(auth);
        List<Ingreso> list = (mes != null && anio != null)
                ? ingresoService.getByMes(uid, mes, anio)
                : ingresoService.getAll(uid);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Ingreso>> create(
            @RequestHeader("Authorization") String auth,
            @RequestBody Ingreso ingreso) {
        try {
            Long uid = extractUserId(auth);
            return ResponseEntity.ok(ApiResponse.ok("Ingreso creado", ingresoService.save(uid, ingreso)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Ingreso>> update(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long id,
            @RequestBody Ingreso ingreso) {
        try {
            Long uid = extractUserId(auth);
            return ResponseEntity.ok(ApiResponse.ok("Ingreso actualizado", ingresoService.update(uid, id, ingreso)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long id) {
        try {
            Long uid = extractUserId(auth);
            ingresoService.delete(uid, id);
            return ResponseEntity.ok(ApiResponse.ok("Ingreso eliminado", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/grafica/{anio}")
    public ResponseEntity<ApiResponse<List<Object[]>>> getGrafica(
            @RequestHeader("Authorization") String auth,
            @PathVariable Integer anio) {
        Long uid = extractUserId(auth);
        return ResponseEntity.ok(ApiResponse.ok(ingresoService.getGraficaAnual(uid, anio)));
    }

    private Long extractUserId(String auth) {
        return jwtUtil.extractUserId(auth.replace("Bearer ", ""));
    }
}
