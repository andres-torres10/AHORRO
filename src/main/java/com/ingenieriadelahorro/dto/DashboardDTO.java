package com.ingenieriadelahorro.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {
    private BigDecimal totalIngresos;
    private BigDecimal totalGastosFijos;
    private BigDecimal totalGastosHormiga;
    private BigDecimal totalDeudas;
    private BigDecimal ahorroDisponible;
    private BigDecimal balanceNeto;
    private String semaforo; // VERDE, AMARILLO, ROJO
    private String mensajeSemaforo;
    private Double porcentajeGastado;
    private Double porcentajeAhorro;
    private List<String> consejos;
    private List<MetaAhorroResumenDTO> metas;
    private Integer mes;
    private Integer anio;
}
