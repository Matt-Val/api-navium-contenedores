package com.navium.contenedores.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.navium.contenedores.model.Contenedor;

@Repository
public interface ContenedorRepository  extends JpaRepository<Contenedor, Long> {

    // Método para el endpoint /patio
    List<Contenedor> findByEstadoGeneral(String estadoGeneral);
}
