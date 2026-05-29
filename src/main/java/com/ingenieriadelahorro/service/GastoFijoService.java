package com.ingenieriadelahorro.service;

import com.ingenieriadelahorro.model.GastoFijo;
import com.ingenieriadelahorro.model.Usuario;
import com.ingenieriadelahorro.repository.GastoFijoRepository;
import com.ingenieriadelahorro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GastoFijoService {

    @Autowired private GastoFijoRepository gastoFijoRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    public List<GastoFijo> getAll(Long usuarioId) {
        return gastoFijoRepository.findByUsuarioIdOrderByFechaPagoDesc(usuarioId);
    }

    public List<GastoFijo> getByMes(Long usuarioId, Integer mes, Integer anio) {
        return gastoFijoRepository.findByUsuarioIdAndMesAndAnioOrderByFechaPagoDesc(usuarioId, mes, anio);
    }

    public List<GastoFijo> getPendientes(Long usuarioId) {
        return gastoFijoRepository.findPendientesByUsuarioId(usuarioId);
    }

    public GastoFijo save(Long usuarioId, GastoFijo gasto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        gasto.setUsuario(usuario);
        return gastoFijoRepository.save(gasto);
    }

    public GastoFijo update(Long usuarioId, Long id, GastoFijo gasto) {
        GastoFijo existing = gastoFijoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gasto fijo no encontrado"));
        if (!existing.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No autorizado");
        }
        existing.setNombre(gasto.getNombre());
        existing.setValor(gasto.getValor());
        existing.setFechaPago(gasto.getFechaPago());
        existing.setDiaPago(gasto.getDiaPago());
        existing.setEstado(gasto.getEstado());
        existing.setPrioridad(gasto.getPrioridad());
        existing.setCategoria(gasto.getCategoria());
        existing.setDescripcion(gasto.getDescripcion());
        return gastoFijoRepository.save(existing);
    }

    public void delete(Long usuarioId, Long id) {
        GastoFijo existing = gastoFijoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gasto fijo no encontrado"));
        if (!existing.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No autorizado");
        }
        gastoFijoRepository.delete(existing);
    }
}
