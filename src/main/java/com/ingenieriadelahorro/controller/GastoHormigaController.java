package com.ingenieriadelahorro.controller;

import com.ingenieriadelahorro.dto.ApiResponse;
import com.ingenieriadelahorro.model.GastoHormiga;
import com.ingenieriadelahorro.security.JwtUtil;
import com.ingenieriadelahorro.service.GastoHormigaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/gastos-hormiga")
@CrossOrigin(origins = "*")
public class GastoHormigaController {

    @Autowired private GastoHormigaService gastoHormigaService;
    @Autowired private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GastoHormiga>>> getAll(
            @RequestHeader("Authorization") String auth,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer anio) {
        Long uid = extractUserId(auth);
        List<GastoHormiga> list = (mes != null && anio != null)
                ? gastoHormigaService.getByMes(uid, mes, anio)
                : gastoHormigaService.getAll(uid);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GastoHormiga>> create(
            @RequestHeader("Authorization") String auth, @RequestBody GastoHormiga gasto) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Creado", gastoHormigaService.save(extractUserId(auth), gasto)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GastoHormiga>> update(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long id, @RequestBody GastoHormiga gasto) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Actualizado", gastoHormigaService.update(extractUserId(auth), id, gasto)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @RequestHeader("Authorization") String auth, @PathVariable Long id) {
        try {
            gastoHormigaService.delete(extractUserId(auth), id);
            return ResponseEntity.ok(ApiResponse.ok("Eliminado", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/grafica/{anio}")
    public ResponseEntity<ApiResponse<List<Object[]>>> getGrafica(
            @RequestHeader("Authorization") String auth, @PathVariable Integer anio) {
        return ResponseEntity.ok(ApiResponse.ok(gastoHormigaService.getGraficaAnual(extractUserId(auth), anio)));
    }

    private Long extractUserId(String auth) {
        return jwtUtil.extractUserId(auth.replace("Bearer ", ""));
    }
}
