package com.navium.contenedores.model;

import jakarta.persistence.*;
import lombok.Data;

@Data // Genera getters, setters y constructores
@Entity
@Table(name = "contenedores")
public class Contenedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 11)
    private String codigoSigla;

    @Column(nullable = false)
    private String tipoCarga; // "Refrigerado" - "Seco" - "Inflamable"

    @Column(nullable = false)
    private String rutEmpresaTransporte; // Para saber a quien pertenece

    // Estados legales
    @Column(nullable = false)
    private String estadoBL = "RETENIDO"; // Por defecto

    @Column(nullable = false)
    private String estadoTATC = "RETENIDO"; // Por defecto

    // Ubicación
    @Column(name = "ubicacion_anden")
    private String ubicacionAnden; // "A-12" - "EN ESPERA"



}
