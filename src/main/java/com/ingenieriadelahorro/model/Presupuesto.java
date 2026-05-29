package com.ingenieriadelahorro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "presupuestos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Presupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotBlank
    @Column(nullable = false)
    private String categoria;

    @NotNull
    @Positive
    @Column(name = "valor_maximo", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorMaximo;

    @Column(name = "valor_gastado", precision = 15, scale = 2)
    private BigDecimal valorGastado = BigDecimal.ZERO;

    @NotNull
    private Integer mes;

    @NotNull
    private Integer anio;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String color = "#1B4F72";
}
