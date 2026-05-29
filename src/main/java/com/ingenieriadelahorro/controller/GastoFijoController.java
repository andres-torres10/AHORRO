package com.ingenieriadelahorro.controller;

import com.ingenieriadelahorro.dto.ApiResponse;
import com.ingenieriadelahorro.model.GastoFijo;
import com.ingenieriadelahorro.security.JwtUtil;
import com.ingenieriadelahorro.service.GastoFijoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/gastos-fijos")
@CrossOrigin(origins = "*")
public class GastoFijoController {

    @Autowired private GastoFijoService gastoFijoService;
    @Autowired private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GastoFijo>>> getAll(
            @RequestHeader("Authorization") String auth,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer anio) {
        Long uid = extractUserId(auth);
        List<GastoFijo> list = (mes != null && anio != null)
                ? gastoFijoService.getByMes(uid, mes, anio)
                : gastoFijoService.getAll(uid);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<ApiResponse<List<GastoFijo>>> getPendientes(@RequestHeader("Authorization") String auth) {
        Long uid = extractUserId(auth);
        return ResponseEntity.ok(ApiResponse.ok(gastoFijoService.getPendientes(uid)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GastoFijo>> create(
            @RequestHeader("Authorization") String auth,
            @RequestBody GastoFijo gasto) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Gasto fijo creado", gastoFijoService.save(extractUserId(auth), gasto)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GastoFijo>> update(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long id, @RequestBody GastoFijo gasto) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Actualizado", gastoFijoService.update(extractUserId(auth), id, gasto)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @RequestHeader("Authorization") String auth, @PathVariable Long id) {
        try {
            gastoFijoService.delete(extractUserId(auth), id);
            return ResponseEntity.ok(ApiResponse.ok("Eliminado", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    private Long extractUserId(String auth) {
        return jwtUtil.extractUserId(auth.replace("Bearer ", ""));
    }
}
