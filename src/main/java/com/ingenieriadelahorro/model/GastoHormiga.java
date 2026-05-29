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
@Table(name = "gastos_hormiga")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GastoHormiga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @NotBlank
    private String categoria; // Comida, Entretenimiento, Bebidas, Transporte, Otros

    @NotNull
    @Positive
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @NotNull
    private LocalDate fecha;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "mes")
    private Integer mes;

    @Column(name = "anio")
    private Integer anio;

    @PrePersist
    @PreUpdate
    public void calcularMesAnio() {
        if (fecha != null) {
            this.mes = fecha.getMonthValue();
            this.anio = fecha.getYear();
        }
    }
}
