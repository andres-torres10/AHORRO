package com.ingenieriadelahorro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "deudas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deuda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotBlank
    @Column(nullable = false)
    private String acreedor;

    @NotNull
    @Positive
    @Column(name = "valor_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "valor_pagado", precision = 15, scale = 2)
    private BigDecimal valorPagado = BigDecimal.ZERO;

    @Column(name = "cuota_mensual", precision = 15, scale = 2)
    private BigDecimal cuotaMensual;

    @Column(precision = 5, scale = 2)
    private BigDecimal interes = BigDecimal.ZERO;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_limite")
    private LocalDate fechaLimite;

    private String estado = "Activa"; // Activa, Pagada, Vencida

    private String tipo; // Tarjeta, Préstamo, Personal, Hipoteca, Otro

    @Column(columnDefinition = "TEXT")
    private String descripcion;
}
