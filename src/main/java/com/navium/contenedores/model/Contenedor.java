package com.navium.contenedores.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Data 
@Entity
@Table(name = "contenedores", schema = "navium")
@Schema(description = "Entidad que representa un contenedor físico y su estado legal dentro del puerto.")
public class Contenedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autogenerado por la base de datos", example = "1")
    private Long id;

    @Column(name = "codigo_sigla", unique = true, nullable = false, length = 11)
    @Schema(description = "Código internacional único de 11 caracteres (4 letras + 7 números)", example = "NAVU1234567")
    private String codigoSigla;

    @Column(name = "tipo_carga", nullable = false)
    @Schema(description = "Naturaleza de la carga contenida", example = "REFRIGERADO", allowableValues = {"SECO", "REFRIGERADO", "INFLAMABLE"})
    private String tipoCarga; 

    @Column(name = "rut_empresa_transporte", nullable = false)
    @Schema(description = "RUT de la empresa responsable del transporte", example = "76.543.210-9")
    private String rutEmpresaTransporte; 

    // Estados legales
    @Column(name = "estado_bl", nullable = false)
    @Schema(description = "Estado legal del Bill of Lading (Aduanas)", example = "RETENIDO", allowableValues = {"RETENIDO", "LIBERADO"})
    private String estadoBL = "RETENIDO"; 

    @Column(name = "estado_tatc", nullable = false)
    @Schema(description = "Estado del Título de Admisión Temporal (Aduanas)", example = "RETENIDO", allowableValues = {"RETENIDO", "LIBERADO"})
    private String estadoTATC = "RETENIDO"; 

    @Column(name = "estado_general", nullable = false)
    @Schema(description = "Estado operativo actual del contenedor en el puerto", example = "REGISTRADO", allowableValues = {"REGISTRADO", "EN PATIO", "DESPACHADO", "DETENIDO"})
    private String estadoGeneral = "REGISTRADO"; 

}
