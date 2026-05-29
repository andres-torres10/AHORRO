package com.ingenieriadelahorro.repository;

import com.ingenieriadelahorro.model.MetaAhorro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MetaAhorroRepository extends JpaRepository<MetaAhorro, Long> {

    List<MetaAhorro> findByUsuarioIdOrderByFechaObjetivoAsc(Long usuarioId);

    List<MetaAhorro> findByUsuarioIdAndEstado(Long usuarioId, String estado);
}
