package com.ingenieriadelahorro.repository;

import com.ingenieriadelahorro.model.Presupuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {

    List<Presupuesto> findByUsuarioIdAndMesAndAnio(Long usuarioId, Integer mes, Integer anio);

    List<Presupuesto> findByUsuarioId(Long usuarioId);

    Optional<Presupuesto> findByUsuarioIdAndCategoriaAndMesAndAnio(Long usuarioId, String categoria, Integer mes, Integer anio);
}
