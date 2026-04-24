package com.navium.contenedores.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.navium.contenedores.model.Contenedor;
import com.navium.contenedores.repository.ContenedorRepository;

@Service
public class ContenedorService {

    @Autowired
    private ContenedorRepository contenedorRepository;
    
    // 1. POST - Registrar la llegada
    public Contenedor registrarContenedor(Contenedor contenedor) { 
        return contenedorRepository.save(contenedor);
    }

    // 2. GET (All) - Listar todos los contenedores
    public List<Contenedor> obtenerTodos() { 
        return contenedorRepository.findAll();
    }

    // 3. GET (ID) - Obtener el detalle de un contenedor por su ID
    public Contenedor obtenerPorId(Long id) {
        return contenedorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contenedor no encontrado"));
    }

    // 4. GET (Patio) - Obtener contenedores que están en el patio
    public List<Contenedor> obtenerOcupacionPatio() { 
        return contenedorRepository.findByUbicacionAndenIsNotNull();
    }

    // 5. PUT (Estado) - Actualizar estados legales
    public Contenedor actualizarEstadoLegal(Long id, String estadoBL, String estadoTATC) { 
        Contenedor contenedor = obtenerPorId(id);

        if (estadoBL != null) contenedor.setEstadoBL(estadoBL);
        if (estadoTATC != null) contenedor.setEstadoTATC(estadoTATC);

        return contenedorRepository.save(contenedor);
    }

    // 6. PUT (Anden) - Actualizar la ubicación física
    public Contenedor asignarAnden(Long id, String ubicacionAnden) { 
        Contenedor contenedor = obtenerPorId(id);
        contenedor.setUbicacionAnden(ubicacionAnden);
        return contenedorRepository.save(contenedor);
    }

    // 7. DELETE - Eliminar un contenedor por su ID
    public void eliminarContenedor(Long id) { 
        contenedorRepository.deleteById(id);
    }
}
