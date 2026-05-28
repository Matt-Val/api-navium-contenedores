package com.navium.contenedores.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.navium.contenedores.model.Contenedor;
import com.navium.contenedores.repository.ContenedorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContenedorServiceTest {

    @Mock
    private ContenedorRepository repository;

    @InjectMocks
    private ContenedorService service;

    @Test
    void registrarContenedor_asignaEstadoPorDefecto() {
        Contenedor contenedor = new Contenedor();
        contenedor.setEstadoGeneral(null);

        when(repository.save(contenedor)).thenReturn(contenedor);

        Contenedor result = service.registrarContenedor(contenedor);

        assertEquals("REGISTRADO", result.getEstadoGeneral());
        verify(repository).save(contenedor);
    }

    @Test
    void obtenerTodos_ok() {
        when(repository.findAll()).thenReturn(List.of(new Contenedor()));

        assertEquals(1, service.obtenerTodos().size());
    }

    @Test
    void obtenerPorId_noEncontrado_lanzaExcepcion() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.obtenerPorId(1L));
    }

    @Test
    void obtenerOcupacionPatio_ok() {
        when(repository.findByEstadoGeneral("EN PATIO")).thenReturn(List.of(new Contenedor()));

        assertEquals(1, service.obtenerOcupacionPatio().size());
    }

    @Test
    void actualizarEstadoLegal_actualizaSoloCamposNoNulos() {
        Contenedor contenedor = new Contenedor();
        contenedor.setId(1L);
        contenedor.setEstadoBL("RETENIDO");
        contenedor.setEstadoTATC("RETENIDO");
        contenedor.setEstadoGeneral("REGISTRADO");

        when(repository.findById(1L)).thenReturn(Optional.of(contenedor));
        when(repository.save(contenedor)).thenReturn(contenedor);

        Contenedor result = service.actualizarEstadoLegal(1L, "OK", null, "EN PATIO");

        assertEquals("OK", result.getEstadoBL());
        assertEquals("RETENIDO", result.getEstadoTATC());
        assertEquals("EN PATIO", result.getEstadoGeneral());
    }

    @Test
    void eliminarContenedor_ok() {
        service.eliminarContenedor(1L);

        verify(repository).deleteById(1L);
    }
}
