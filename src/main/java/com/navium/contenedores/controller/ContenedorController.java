package com.navium.contenedores.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;

import com.navium.contenedores.model.Contenedor;
import com.navium.contenedores.service.ContenedorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/contenedores")
@Tag(name="Gestión de Contenedores", description = "Endpoints para registrar, listar, actualizar y eliminar contenedores en el patio")
@SecurityRequirement(name = "bearerAuth")
public class ContenedorController {

    @Autowired
    private ContenedorService service;
    
    // 1. POST - Registrar la llegada
    @PostMapping
    @Operation(summary = "Registrar llegada", description = "Ingresa un nuevo contenedor al sistema con su información inicial")
    public ResponseEntity<?> crear(@RequestBody Contenedor contenedor) {
        try {
            // Intenta guardarlo en la base de datos
            return ResponseEntity.ok(service.registrarContenedor(contenedor));
        } catch (DataIntegrityViolationException e) {
            // Si PostgreSQL detecta que la sigla ya existe, explota aquí.
            // Atrapamos la explosión y armamos una respuesta con código 409.
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Conflicto de Datos");
            errorResponse.put("mensaje", "El contenedor con esa sigla ya se encuentra registrado.");
            
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    // 2. GET (All) - Listar todos los contenedores
    @GetMapping
    @Operation(summary = "Listar todos los contenedores", description = "Obtiene el historial completo de contenedores registrados.")
    public ResponseEntity<List<Contenedor>> listarTodos() { 
        return ResponseEntity.ok(service.obtenerTodos());
    }

    // 3. GET (ID) - Obtener el detalle de un contenedor por su ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener contenedor por ID", description = "Obtiene los detalles de un contenedor específico por su ID.")
    public ResponseEntity<Contenedor> obtenerPorId(@PathVariable Long id) { 
        try { 
            Contenedor contenedor =service.obtenerPorId(id);
            return ResponseEntity.ok(contenedor);
        } catch (Exception e) { 
            // Si el contenedor no se encuentra, se devuelve un 204 No Content
            return ResponseEntity.notFound().build();
        }
    }

    // 4. GET (Patio) - Obtener contenedores que están en el patio
    @GetMapping("/patio")
    @Operation(summary = "Obtener contenedores en el patio", description = "Obtiene la lista de contenedores que se encuentran en el patio.")
    public ResponseEntity<List<Contenedor>> obtenerPatio() { 
        return ResponseEntity.ok(service.obtenerOcupacionPatio());
    }

    // 5. PUT (Estado) - Actualizar estados legales
    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del contenedor", description = "Actualiza los estados legales de un contenedor específico.")
    public ResponseEntity<Contenedor> actualizarEstado(
            @PathVariable Long id,
            @RequestParam(required = false) String estadoBL,
            @RequestParam(required = false) String estadoTATC,
            @RequestParam(required = false) String estadoGeneral
        ) { 
            return ResponseEntity.ok(service.actualizarEstadoLegal(id, estadoBL, estadoTATC, estadoGeneral));
    }
    
    // 6. DELETE - Eliminar un contenedor por su ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar contenedor", description = "Elimina un contenedor del sistema por su ID.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) { 
        service.eliminarContenedor(id);
        return ResponseEntity.noContent().build();
    }
}
