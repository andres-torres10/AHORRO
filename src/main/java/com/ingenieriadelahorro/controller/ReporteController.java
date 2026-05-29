package com.ingenieriadelahorro.controller;

import com.ingenieriadelahorro.security.JwtUtil;
import com.ingenieriadelahorro.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired private ReporteService reporteService;
    @Autowired private JwtUtil jwtUtil;

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> descargarPDF(
            @RequestHeader("Authorization") String auth,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer anio) {
        try {
            Long uid = extractUserId(auth);
            if (mes == null) mes = LocalDate.now().getMonthValue();
            if (anio == null) anio = LocalDate.now().getYear();
            byte[] pdf = reporteService.generarPDF(uid, mes, anio);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename("reporte_" + mes + "_" + anio + ".pdf").build());
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/excel")
    public ResponseEntity<byte[]> descargarExcel(
            @RequestHeader("Authorization") String auth,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer anio) {
        try {
            Long uid = extractUserId(auth);
            if (mes == null) mes = LocalDate.now().getMonthValue();
            if (anio == null) anio = LocalDate.now().getYear();
            byte[] excel = reporteService.generarExcel(uid, mes, anio);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename("reporte_" + mes + "_" + anio + ".xlsx").build());
            return new ResponseEntity<>(excel, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private Long extractUserId(String auth) {
        return jwtUtil.extractUserId(auth.replace("Bearer ", ""));
    }
}
