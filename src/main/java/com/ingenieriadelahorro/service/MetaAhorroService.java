package com.ingenieriadelahorro.service;

import com.ingenieriadelahorro.model.MetaAhorro;
import com.ingenieriadelahorro.model.Usuario;
import com.ingenieriadelahorro.repository.MetaAhorroRepository;
import com.ingenieriadelahorro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MetaAhorroService {

    @Autowired private MetaAhorroRepository metaAhorroRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public List<MetaAhorro> getAll(Long usuarioId) {
        return metaAhorroRepository.findByUsuarioIdOrderByFechaObjetivoAsc(usuarioId);
    }

    public MetaAhorro save(Long usuarioId, MetaAhorro meta) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        meta.setUsuario(usuario);
        return metaAhorroRepository.save(meta);
    }

    public MetaAhorro update(Long usuarioId, Long id, MetaAhorro meta) {
        MetaAhorro existing = metaAhorroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));
        if (!existing.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No autorizado");
        }
        existing.setNombre(meta.getNombre());
        existing.setValorMeta(meta.getValorMeta());
        existing.setValorAhorrado(meta.getValorAhorrado());
        existing.setFechaObjetivo(meta.getFechaObjetivo());
        existing.setEstado(meta.getEstado());
        existing.setIcono(meta.getIcono());
        existing.setColor(meta.getColor());
        existing.setDescripcion(meta.getDescripcion());
        return metaAhorroRepository.save(existing);
    }

    public void delete(Long usuarioId, Long id) {
        MetaAhorro existing = metaAhorroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));
        if (!existing.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No autorizado");
        }
        metaAhorroRepository.delete(existing);
    }
}
