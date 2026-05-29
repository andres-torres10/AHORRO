package com.ingenieriadelahorro.service;

import com.ingenieriadelahorro.model.GastoHormiga;
import com.ingenieriadelahorro.model.Usuario;
import com.ingenieriadelahorro.repository.GastoHormigaRepository;
import com.ingenieriadelahorro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GastoHormigaService {

    @Autowired private GastoHormigaRepository gastoHormigaRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public List<GastoHormiga> getAll(Long usuarioId) {
        return gastoHormigaRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    public List<GastoHormiga> getByMes(Long usuarioId, Integer mes, Integer anio) {
        return gastoHormigaRepository.findByUsuarioIdAndMesAndAnioOrderByFechaDesc(usuarioId, mes, anio);
    }

    public GastoHormiga save(Long usuarioId, GastoHormiga gasto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        gasto.setUsuario(usuario);
        return gastoHormigaRepository.save(gasto);
    }

    public GastoHormiga update(Long usuarioId, Long id, GastoHormiga gasto) {
        GastoHormiga existing = gastoHormigaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gasto hormiga no encontrado"));
        if (!existing.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No autorizado");
        }
        existing.setNombre(gasto.getNombre());
        existing.setCategoria(gasto.getCategoria());
        existing.setValor(gasto.getValor());
        existing.setFecha(gasto.getFecha());
        existing.setDescripcion(gasto.getDescripcion());
        return gastoHormigaRepository.save(existing);
    }

    public void delete(Long usuarioId, Long id) {
        GastoHormiga existing = gastoHormigaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gasto hormiga no encontrado"));
        if (!existing.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No autorizado");
        }
        gastoHormigaRepository.delete(existing);
    }

    public List<Object[]> getGraficaAnual(Long usuarioId, Integer anio) {
        return gastoHormigaRepository.sumByMesAndAnio(usuarioId, anio);
    }
}
