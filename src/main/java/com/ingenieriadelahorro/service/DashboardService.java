package com.ingenieriadelahorro.service;

import com.ingenieriadelahorro.dto.DashboardDTO;
import com.ingenieriadelahorro.dto.MetaAhorroResumenDTO;
import com.ingenieriadelahorro.model.MetaAhorro;
import com.ingenieriadelahorro.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    @Autowired private IngresoRepository ingresoRepository;
    @Autowired private GastoFijoRepository gastoFijoRepository;
    @Autowired private GastoHormigaRepository gastoHormigaRepository;
    @Autowired private DeudaRepository deudaRepository;
    @Autowired private MetaAhorroRepository metaAhorroRepository;

    public DashboardDTO getDashboard(Long usuarioId, Integer mes, Integer anio) {
        DashboardDTO dto = new DashboardDTO();
        dto.setMes(mes);
        dto.setAnio(anio);

        BigDecimal ingresos = ingresoRepository.sumByUsuarioIdAndMesAndAnio(usuarioId, mes, anio);
        BigDecimal gastosFijos = gastoFijoRepository.sumByUsuarioIdAndMesAndAnio(usuarioId, mes, anio);
        BigDecimal gastosHormiga = gastoHormigaRepository.sumByUsuarioIdAndMesAndAnio(usuarioId, mes, anio);
        BigDecimal deudas = deudaRepository.sumDeudaPendienteByUsuarioId(usuarioId);
        BigDecimal cuotasDeuda = deudaRepository.sumCuotasMensualesByUsuarioId(usuarioId);

        BigDecimal totalGastos = gastosFijos.add(gastosHormiga).add(cuotasDeuda);
        BigDecimal ahorro = ingresos.subtract(totalGastos);
        BigDecimal balance = ingresos.subtract(totalGastos);

        dto.setTotalIngresos(ingresos);
        dto.setTotalGastosFijos(gastosFijos);
        dto.setTotalGastosHormiga(gastosHormiga);
        dto.setTotalDeudas(deudas);
        dto.setAhorroDisponible(ahorro.compareTo(BigDecimal.ZERO) > 0 ? ahorro : BigDecimal.ZERO);
        dto.setBalanceNeto(balance);

        // Porcentaje gastado
        double pctGastado = 0;
        if (ingresos.compareTo(BigDecimal.ZERO) > 0) {
            pctGastado = totalGastos.divide(ingresos, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)).doubleValue();
        }
        dto.setPorcentajeGastado(pctGastado);
        dto.setPorcentajeAhorro(Math.max(0, 100 - pctGastado));

        // Semáforo
        String semaforo;
        String mensaje;
        if (pctGastado <= 70) {
            semaforo = "VERDE";
            mensaje = "¡Excelente! Tus finanzas están saludables. Sigues ahorrando bien.";
        } else if (pctGastado <= 90) {
            semaforo = "AMARILLO";
            mensaje = "Atención: tus gastos están ajustados. Revisa tus gastos hormiga.";
        } else {
            semaforo = "ROJO";
            mensaje = "¡Peligro! Tus gastos superan tus ingresos. Actúa de inmediato.";
        }
        dto.setSemaforo(semaforo);
        dto.setMensajeSemaforo(mensaje);

        // Consejos automáticos
        List<String> consejos = new ArrayList<>();
        if (gastosHormiga.compareTo(ingresos.multiply(BigDecimal.valueOf(0.15))) > 0) {
            consejos.add("🐜 Tus gastos hormiga están altos. Representan más del 15% de tus ingresos.");
        }
        if (pctGastado > 80) {
            consejos.add("💡 Podrías ahorrar un 15% más reduciendo gastos no esenciales.");
        }
        if (deudas.compareTo(ingresos.multiply(BigDecimal.valueOf(3))) > 0) {
            consejos.add("⚠️ Tu deuda total supera 3 meses de ingresos. Considera un plan de pago.");
        }
        if (ahorro.compareTo(BigDecimal.ZERO) > 0) {
            consejos.add("✅ Tienes capacidad de ahorro. Considera invertir el excedente.");
        }
        if (consejos.isEmpty()) {
            consejos.add("🎯 ¡Vas muy bien! Mantén tus hábitos financieros actuales.");
        }
        dto.setConsejos(consejos);

        // Metas de ahorro
        List<MetaAhorro> metas = metaAhorroRepository.findByUsuarioIdAndEstado(usuarioId, "En progreso");
        List<MetaAhorroResumenDTO> metasDTO = new ArrayList<>();
        for (MetaAhorro meta : metas) {
            MetaAhorroResumenDTO m = new MetaAhorroResumenDTO();
            m.setId(meta.getId());
            m.setNombre(meta.getNombre());
            m.setValorMeta(meta.getValorMeta());
            m.setValorAhorrado(meta.getValorAhorrado());
            m.setIcono(meta.getIcono());
            m.setColor(meta.getColor());
            m.setEstado(meta.getEstado());
            double pct = 0;
            if (meta.getValorMeta().compareTo(BigDecimal.ZERO) > 0) {
                pct = meta.getValorAhorrado().divide(meta.getValorMeta(), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)).doubleValue();
            }
            m.setPorcentaje(Math.min(100, pct));
            metasDTO.add(m);
        }
        dto.setMetas(metasDTO);

        return dto;
    }
}
