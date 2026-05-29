package com.ingenieriadelahorro.service;

import com.ingenieriadelahorro.model.Presupuesto;
import com.ingenieriadelahorro.model.Usuario;
import com.ingenieriadelahorro.repository.PresupuestoRepository;
import com.ingenieriadelahorro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PresupuestoService {

    @Autowired private PresupuestoRepository presupuestoRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public List<Presupuesto> getByMes(Long usuarioId, Integer mes, Integer anio) {
        return presupuestoRepository.findByUsuarioIdAndMesAndAnio(usuarioId, mes, anio);
    }

    public List<Presupuesto> getAll(Long usuarioId) {
        return presupuestoRepository.findByUsuarioId(usuarioId);
    }

    public Presupuesto save(Long usuarioId, Presupuesto presupuesto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        presupuesto.setUsuario(usuario);
        return presupuestoRepository.save(presupuesto);
    }

    public Presupuesto update(Long usuarioId, Long id, Presupuesto presupuesto) {
        Presupuesto existing = presupuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado"));
        if (!existing.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No autorizado");
        }
        existing.setCategoria(presupuesto.getCategoria());
        existing.setValorMaximo(presupuesto.getValorMaximo());
        existing.setValorGastado(presupuesto.getValorGastado());
        existing.setDescripcion(presupuesto.getDescripcion());
        existing.setColor(presupuesto.getColor());
        return presupuestoRepository.save(existing);
    }

    public void delete(Long usuarioId, Long id) {
        Presupuesto existing = presupuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado"));
        if (!existing.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No autorizado");
        }
        presupuestoRepository.delete(existing);
    }
}
