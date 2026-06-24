package com.navium.contenedores.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Map;

import com.navium.contenedores.model.Contenedor;
import com.navium.contenedores.service.ContenedorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ContenedorControllerTest {

    @Mock
    private ContenedorService service;

    @InjectMocks
    private ContenedorController controller;

    @Test
    void crear_ok() {
        Contenedor contenedor = new Contenedor();
        contenedor.setCodigoSigla("ABCD1234567");
        contenedor.setTipoCarga("Seco");
        contenedor.setRutEmpresaTransporte("12345678-9");

        given(service.registrarContenedor(any(Contenedor.class))).willReturn(contenedor);

        ResponseEntity<?> response = controller.crear(contenedor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Contenedor body = (Contenedor) response.getBody();
        assertNotNull(body);
        assertEquals("ABCD1234567", body.getCodigoSigla());
    }

    @Test
    void crear_conflicto() {
        given(service.registrarContenedor(any(Contenedor.class)))
            .willThrow(new DataIntegrityViolationException("dup"));

        ResponseEntity<?> response = controller.crear(new Contenedor());

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertNotNull(body);
        assertEquals("Conflicto de Datos", body.get("error"));
    }

    @Test
    void listarTodos_ok() {
        given(service.obtenerTodos()).willReturn(List.of(new Contenedor(), new Contenedor()));

        ResponseEntity<List<Contenedor>> response = controller.listarTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void obtenerPorId_encontrado() {
        Contenedor contenedor = new Contenedor();
        contenedor.setId(1L);
        contenedor.setCodigoSigla("ABCD1234567");

        given(service.obtenerPorId(1L)).willReturn(contenedor);

        ResponseEntity<Contenedor> response = controller.obtenerPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void obtenerPorId_noEncontrado() {
        given(service.obtenerPorId(99L)).willThrow(new RuntimeException("no"));

        ResponseEntity<Contenedor> response = controller.obtenerPorId(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void obtenerPatio_ok() {
        given(service.obtenerOcupacionPatio()).willReturn(List.of(new Contenedor()));

        ResponseEntity<List<Contenedor>> response = controller.obtenerPatio();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void actualizarEstado_ok() {
        Contenedor contenedor = new Contenedor();
        contenedor.setId(1L);
        contenedor.setEstadoGeneral("EN PATIO");

        given(service.actualizarEstadoLegal(1L, "OK", "OK", "EN PATIO"))
            .willReturn(contenedor);

        ResponseEntity<?> response = controller.actualizarEstado(1L, "OK", "OK", "EN PATIO");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("EN PATIO", ((Contenedor)response.getBody()).getEstadoGeneral());
    }

    @Test
    void eliminar_ok() {
        ResponseEntity<?> response = controller.eliminar(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}