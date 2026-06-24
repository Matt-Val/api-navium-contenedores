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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/contenedores")
@Tag(name="Gestión de Contenedores", description = "Endpoints para el ciclo de vida de los contenedores: Registro, Trazabilidad y Estados Legales")
public class ContenedorController {

    @Autowired
    private ContenedorService service;
    
    // 1. POST - Registrar la llegada
    @PostMapping
    @Operation(summary = "Registrar manifiesto de llegada", description = "Crea un nuevo registro de contenedor en el sistema. Valida que la sigla sea única y los estados iniciales sean válidos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contenedor registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
        @ApiResponse(responseCode = "409", description = "Conflicto: La sigla del contenedor ya existe en el sistema")
    })
    public ResponseEntity<?> crear(@RequestBody Contenedor contenedor) {
        try {
            // Intenta guardarlo en la base de datos
            return ResponseEntity.ok(service.registrarContenedor(contenedor));
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Validacion de datos");
            errorResponse.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (DataIntegrityViolationException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Conflicto de Datos");
            errorResponse.put("mensaje", "El contenedor con esa sigla ya se encuentra registrado.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    // 2. GET (All) - Listar todos los contenedores
    @GetMapping
    @Operation(summary = "Listar inventario completo", description = "Recupera todos los contenedores registrados en el sistema, independiente de su estado.")
    @ApiResponse(responseCode = "200", description = "Lista de contenedores obtenida exitosamente")
    public ResponseEntity<List<Contenedor>> listarTodos() { 
        return ResponseEntity.ok(service.obtenerTodos());
    }

    // 3. GET (ID) - Obtener el detalle de un contenedor por su ID
    @GetMapping("/{id}")
    @Operation(summary = "Consultar detalle por ID", description = "Obtiene la ficha técnica completa y estados legales de un contenedor específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contenedor encontrado"),
        @ApiResponse(responseCode = "404", description = "Contenedor no localizado")
    })
    public ResponseEntity<Contenedor> obtenerPorId(@PathVariable Long id) { 
        try { 
            Contenedor contenedor = service.obtenerPorId(id);
            return ResponseEntity.ok(contenedor);
        } catch (Exception e) { 
            return ResponseEntity.notFound().build();
        }
    }

    // 4. GET (Patio) - Obtener contenedores que están en el patio
    @GetMapping("/patio")
    @Operation(summary = "Ver ocupación de patio", description = "Filtra y retorna solo los contenedores que tienen el estado 'EN PATIO'.")
    @ApiResponse(responseCode = "200", description = "Lista de ocupación obtenida exitosamente")
    public ResponseEntity<List<Contenedor>> obtenerPatio() { 
        return ResponseEntity.ok(service.obtenerOcupacionPatio());
    }

    // 5. PUT (Estado) - Actualizar estados legales
    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estados legales", description = "Permite a Aduanas o el Centro de Mando liberar/retener el BL o TATC, o cambiar el estado general.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estados actualizados correctamente"),
        @ApiResponse(responseCode = "400", description = "Valores de estado no permitidos"),
        @ApiResponse(responseCode = "404", description = "Contenedor no encontrado")
    })
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long id,
            @RequestParam(required = false) String estadoBL,
            @RequestParam(required = false) String estadoTATC,
            @RequestParam(required = false) String estadoGeneral
        ) { 
            try {
                return ResponseEntity.ok(service.actualizarEstadoLegal(id, estadoBL, estadoTATC, estadoGeneral));
            } catch (IllegalArgumentException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Validacion de datos");
                errorResponse.put("mensaje", e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
    }
    
    // 6. DELETE - Eliminar un contenedor por su ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar registro", description = "Borra físicamente el registro del contenedor. Fallará si tiene dependencias activas (ej: agendamientos).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Contenedor eliminado exitosamente"),
        @ApiResponse(responseCode = "409", description = "No se puede eliminar: El contenedor tiene registros relacionados")
    })
    public ResponseEntity<?> eliminar(@PathVariable Long id) { 
        try {
            service.eliminarContenedor(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Conflicto de Datos");
            errorResponse.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }
}
