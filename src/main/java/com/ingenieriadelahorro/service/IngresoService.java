package com.ingenieriadelahorro.service;

import com.ingenieriadelahorro.model.Ingreso;
import com.ingenieriadelahorro.model.Usuario;
import com.ingenieriadelahorro.repository.IngresoRepository;
import com.ingenieriadelahorro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IngresoService {

    @Autowired private IngresoRepository ingresoRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public List<Ingreso> getAll(Long usuarioId) {
        return ingresoRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    public List<Ingreso> getByMes(Long usuarioId, Integer mes, Integer anio) {
        return ingresoRepository.findByUsuarioIdAndMesAndAnioOrderByFechaDesc(usuarioId, mes, anio);
    }

    public Ingreso save(Long usuarioId, Ingreso ingreso) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        ingreso.setUsuario(usuario);
        return ingresoRepository.save(ingreso);
    }

    public Ingreso update(Long usuarioId, Long id, Ingreso ingreso) {
        Ingreso existing = ingresoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingreso no encontrado"));
        if (!existing.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No autorizado");
        }
        existing.setNombre(ingreso.getNombre());
        existing.setCategoria(ingreso.getCategoria());
        existing.setValor(ingreso.getValor());
        existing.setFecha(ingreso.getFecha());
        existing.setDescripcion(ingreso.getDescripcion());
        existing.setMetodoPago(ingreso.getMetodoPago());
        return ingresoRepository.save(existing);
    }

    public void delete(Long usuarioId, Long id) {
        Ingreso existing = ingresoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingreso no encontrado"));
        if (!existing.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No autorizado");
        }
        ingresoRepository.delete(existing);
    }

    public List<Object[]> getGraficaAnual(Long usuarioId, Integer anio) {
        return ingresoRepository.sumByMesAndAnio(usuarioId, anio);
    }
}
