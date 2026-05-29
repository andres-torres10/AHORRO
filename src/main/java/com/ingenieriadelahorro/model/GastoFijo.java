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
@Table(name = "gastos_fijos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GastoFijo {

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
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    @Column(name = "dia_pago")
    private Integer diaPago;

    private String estado = "Pendiente"; // Pendiente, Pagado, Vencido

    private String prioridad = "Media"; // Alta, Media, Baja

    private String categoria;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "mes")
    private Integer mes;

    @Column(name = "anio")
    private Integer anio;

    @PrePersist
    @PreUpdate
    public void calcularMesAnio() {
        if (fechaPago != null) {
            this.mes = fechaPago.getMonthValue();
            this.anio = fechaPago.getYear();
        }
    }
}
