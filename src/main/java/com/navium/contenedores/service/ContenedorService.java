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

    public Contenedor registrarContenedor(Contenedor contenedor) { 
        if (contenedor.getEstadoGeneral() == null || contenedor.getEstadoGeneral().isEmpty() ) { 
            contenedor.setEstadoGeneral("REGISTRADO");
        }
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
        
        if(estadoBL != null) contenedor.setEstadoBL(estadoBL);
        if(estadoTATC != null) contenedor.setEstadoTATC(estadoTATC);
        if(estadoGeneral != null) contenedor.setEstadoGeneral(estadoGeneral);

        return contenedorRepository.save(contenedor);
    }

    public void eliminarContenedor(Long id) { 
        contenedorRepository.deleteById(id);
    }
}
