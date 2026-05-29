package com.ingenieriadelahorro.repository;

import com.ingenieriadelahorro.model.Ingreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IngresoRepository extends JpaRepository<Ingreso, Long> {

    List<Ingreso> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

    List<Ingreso> findByUsuarioIdAndMesAndAnioOrderByFechaDesc(Long usuarioId, Integer mes, Integer anio);

    @Query("SELECT COALESCE(SUM(i.valor), 0) FROM Ingreso i WHERE i.usuario.id = :usuarioId AND i.mes = :mes AND i.anio = :anio")
    BigDecimal sumByUsuarioIdAndMesAndAnio(@Param("usuarioId") Long usuarioId, @Param("mes") Integer mes, @Param("anio") Integer anio);

    @Query("SELECT i.categoria, SUM(i.valor) FROM Ingreso i WHERE i.usuario.id = :usuarioId AND i.mes = :mes AND i.anio = :anio GROUP BY i.categoria")
    List<Object[]> sumByCategoriaAndMesAndAnio(@Param("usuarioId") Long usuarioId, @Param("mes") Integer mes, @Param("anio") Integer anio);

    @Query("SELECT i.mes, SUM(i.valor) FROM Ingreso i WHERE i.usuario.id = :usuarioId AND i.anio = :anio GROUP BY i.mes ORDER BY i.mes")
    List<Object[]> sumByMesAndAnio(@Param("usuarioId") Long usuarioId, @Param("anio") Integer anio);
}
