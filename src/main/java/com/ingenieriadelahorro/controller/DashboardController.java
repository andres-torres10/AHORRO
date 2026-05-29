package com.ingenieriadelahorro.controller;

import com.ingenieriadelahorro.dto.ApiResponse;
import com.ingenieriadelahorro.dto.DashboardDTO;
import com.ingenieriadelahorro.security.JwtUtil;
import com.ingenieriadelahorro.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired private DashboardService dashboardService;
    @Autowired private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardDTO>> getDashboard(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer anio) {
        try {
            Long userId = extractUserId(authHeader);
            if (mes == null) mes = LocalDate.now().getMonthValue();
            if (anio == null) anio = LocalDate.now().getYear();
            DashboardDTO dto = dashboardService.getDashboard(userId, mes, anio);
            return ResponseEntity.ok(ApiResponse.ok(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    private Long extractUserId(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.extractUserId(token);
    }
}
