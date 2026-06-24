package com.navium.contenedores.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.navium.contenedores.model.Contenedor;
import com.navium.contenedores.repository.ContenedorRepository;

import java.util.Locale;
import java.util.Set;

@Service
public class ContenedorService {

    @Autowired
    private ContenedorRepository contenedorRepository;

    private static final Set<String> ESTADOS_LEGALES_VALIDOS = Set.of("RETENIDO", "LIBERADO");
    private static final Set<String> ESTADOS_GENERALES_VALIDOS = Set.of(
            "REGISTRADO",
            "EN PATIO",
            "DESPACHADO",
            "DETENIDO"
    );

    public Contenedor registrarContenedor(Contenedor contenedor) { 
        contenedor.setEstadoBL(normalizarEstadoLegal(contenedor.getEstadoBL(), "RETENIDO"));
        contenedor.setEstadoTATC(normalizarEstadoLegal(contenedor.getEstadoTATC(), "RETENIDO"));
        contenedor.setEstadoGeneral(normalizarEstadoGeneral(contenedor.getEstadoGeneral(), "REGISTRADO"));
        validarEstados(contenedor.getEstadoBL(), contenedor.getEstadoTATC(), contenedor.getEstadoGeneral());
        return contenedorRepository.save(contenedor);
    }

    public List<Contenedor> obtenerTodos() { 
        return contenedorRepository.findAll();
    }

    public Contenedor obtenerPorId(Long id) { 
        return contenedorRepository.findById(id).orElseThrow( () -> new RuntimeException("Contenedor no encontrado"));
    }

    public List<Contenedor> obtenerOcupacionPatio() { 
        return contenedorRepository.findByEstadoGeneral("EN PATIO");
    }

    public Contenedor actualizarEstadoLegal(Long id, String estadoBL, String estadoTATC, String estadoGeneral) { 
        Contenedor contenedor = obtenerPorId(id);

        if (estadoBL != null) {
            contenedor.setEstadoBL(normalizarEstadoLegal(estadoBL, contenedor.getEstadoBL()));
        }
        if (estadoTATC != null) {
            contenedor.setEstadoTATC(normalizarEstadoLegal(estadoTATC, contenedor.getEstadoTATC()));
        }
        if (estadoGeneral != null) {
            contenedor.setEstadoGeneral(normalizarEstadoGeneral(estadoGeneral, contenedor.getEstadoGeneral()));
        }

        validarEstados(contenedor.getEstadoBL(), contenedor.getEstadoTATC(), contenedor.getEstadoGeneral());

        return contenedorRepository.save(contenedor);
    }

    public void eliminarContenedor(Long id) { 
        try {
            contenedorRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("El contenedor tiene referencias activas y no puede eliminarse.", e);
        }
    }

    private void validarEstados(String estadoBL, String estadoTATC, String estadoGeneral) {
        if (!ESTADOS_LEGALES_VALIDOS.contains(estadoBL)) {
            throw new IllegalArgumentException("Estado BL invalido. Valores permitidos: RETENIDO o LIBERADO.");
        }
        if (!ESTADOS_LEGALES_VALIDOS.contains(estadoTATC)) {
            throw new IllegalArgumentException("Estado TATC invalido. Valores permitidos: RETENIDO o LIBERADO.");
        }
        if (!ESTADOS_GENERALES_VALIDOS.contains(estadoGeneral)) {
            throw new IllegalArgumentException(
                    "Estado general invalido. Valores permitidos: REGISTRADO, EN PATIO, DESPACHADO o DETENIDO."
            );
        }
    }

    private String normalizarEstadoLegal(String estado, String valorPorDefecto) {
        if (estado == null || estado.trim().isEmpty()) {
            return valorPorDefecto;
        }
        return estado.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizarEstadoGeneral(String estado, String valorPorDefecto) {
        if (estado == null || estado.trim().isEmpty()) {
            return valorPorDefecto;
        }
        return estado.trim().toUpperCase(Locale.ROOT);
    }
}
