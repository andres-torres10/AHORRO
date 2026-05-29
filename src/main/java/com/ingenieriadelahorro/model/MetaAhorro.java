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
@Table(name = "metas_ahorro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaAhorro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @NotNull
    @Positive
    @Column(name = "valor_meta", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorMeta;

    @Column(name = "valor_ahorrado", precision = 15, scale = 2)
    private BigDecimal valorAhorrado = BigDecimal.ZERO;

    @Column(name = "fecha_objetivo")
    private LocalDate fechaObjetivo;

    private String estado = "En progreso"; // En progreso, Completada, Cancelada

    private String icono = "🎯";

    private String color = "#27AE60";

    @Column(columnDefinition = "TEXT")
    private String descripcion;
}
