package com.ingenieriadelahorro.repository;

import com.ingenieriadelahorro.model.Deuda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DeudaRepository extends JpaRepository<Deuda, Long> {

    List<Deuda> findByUsuarioIdOrderByFechaLimiteAsc(Long usuarioId);

    List<Deuda> findByUsuarioIdAndEstadoOrderByFechaLimiteAsc(Long usuarioId, String estado);

    @Query("SELECT COALESCE(SUM(d.valorTotal - d.valorPagado), 0) FROM Deuda d WHERE d.usuario.id = :usuarioId AND d.estado = 'Activa'")
    BigDecimal sumDeudaPendienteByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT COALESCE(SUM(d.cuotaMensual), 0) FROM Deuda d WHERE d.usuario.id = :usuarioId AND d.estado = 'Activa'")
    BigDecimal sumCuotasMensualesByUsuarioId(@Param("usuarioId") Long usuarioId);
}
