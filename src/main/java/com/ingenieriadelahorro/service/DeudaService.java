package com.ingenieriadelahorro.service;

import com.ingenieriadelahorro.model.Deuda;
import com.ingenieriadelahorro.model.Usuario;
import com.ingenieriadelahorro.repository.DeudaRepository;
import com.ingenieriadelahorro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeudaService {

    @Autowired private DeudaRepository deudaRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public List<Deuda> getAll(Long usuarioId) {
        return deudaRepository.findByUsuarioIdOrderByFechaLimiteAsc(usuarioId);
    }

    public Deuda save(Long usuarioId, Deuda deuda) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        deuda.setUsuario(usuario);
        return deudaRepository.save(deuda);
    }

    public Deuda update(Long usuarioId, Long id, Deuda deuda) {
        Deuda existing = deudaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada"));
        if (!existing.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No autorizado");
        }
        existing.setAcreedor(deuda.getAcreedor());
        existing.setValorTotal(deuda.getValorTotal());
        existing.setValorPagado(deuda.getValorPagado());
        existing.setCuotaMensual(deuda.getCuotaMensual());
        existing.setInteres(deuda.getInteres());
        existing.setFechaInicio(deuda.getFechaInicio());
        existing.setFechaLimite(deuda.getFechaLimite());
        existing.setEstado(deuda.getEstado());
        existing.setTipo(deuda.getTipo());
        existing.setDescripcion(deuda.getDescripcion());
        return deudaRepository.save(existing);
    }

    public void delete(Long usuarioId, Long id) {
        Deuda existing = deudaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deuda no encontrada"));
        if (!existing.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No autorizado");
        }
        deudaRepository.delete(existing);
    }
}
