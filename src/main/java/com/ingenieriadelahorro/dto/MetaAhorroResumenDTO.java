package com.ingenieriadelahorro.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaAhorroResumenDTO {
    private Long id;
    private String nombre;
    private BigDecimal valorMeta;
    private BigDecimal valorAhorrado;
    private Double porcentaje;
    private String icono;
    private String color;
    private String estado;
}
