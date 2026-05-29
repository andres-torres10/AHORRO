package com.ingenieriadelahorro.repository;

import com.ingenieriadelahorro.model.GastoFijo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface GastoFijoRepository extends JpaRepository<GastoFijo, Long> {

    List<GastoFijo> findByUsuarioIdOrderByFechaPagoDesc(Long usuarioId);

    List<GastoFijo> findByUsuarioIdAndMesAndAnioOrderByFechaPagoDesc(Long usuarioId, Integer mes, Integer anio);

    @Query("SELECT COALESCE(SUM(g.valor), 0) FROM GastoFijo g WHERE g.usuario.id = :usuarioId AND g.mes = :mes AND g.anio = :anio")
    BigDecimal sumByUsuarioIdAndMesAndAnio(@Param("usuarioId") Long usuarioId, @Param("mes") Integer mes, @Param("anio") Integer anio);

    @Query("SELECT g FROM GastoFijo g WHERE g.usuario.id = :usuarioId AND g.estado = 'Pendiente' ORDER BY g.fechaPago ASC")
    List<GastoFijo> findPendientesByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT g.mes, SUM(g.valor) FROM GastoFijo g WHERE g.usuario.id = :usuarioId AND g.anio = :anio GROUP BY g.mes ORDER BY g.mes")
    List<Object[]> sumByMesAndAnio(@Param("usuarioId") Long usuarioId, @Param("anio") Integer anio);
}
