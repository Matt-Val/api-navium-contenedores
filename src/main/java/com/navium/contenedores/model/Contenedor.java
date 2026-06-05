package com.navium.contenedores.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Data 
@Entity
@Table(name = "contenedores")
@Schema(description = "Entidad que representa un contenedor en el sistema de gestión de contenedores.")
public class Contenedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autogenerado de la base de datos")
    private Long id;

    @Column(name = "codigo_sigla", unique = true, nullable = false, length = 11)
    @Schema(description = "Código internacional del contenedor")
    private String codigoSigla;

    @Column(name = "tipo_carga", nullable = false)
    @Schema(description = "Tipo de carga del contenedor")
    private String tipoCarga; // "Refrigerado" - "Seco" - "Inflamable"

    @Column(name = "rut_empresa_transporte", nullable = false)
    @Schema(description = "RUT de la empresa de transporte a la que pertenece el contenedor")
    private String rutEmpresaTransporte; // Para saber a quien pertenece

    // Estados legales
    @Column(name = "estado_bl", nullable = false)
    @Schema(description = "Estado del contenedor en relación con el BL")
    private String estadoBL = "RETENIDO"; // Por defecto

    @Column(name = "estado_tatc", nullable = false)
    @Schema(description = "Estado del contenedor en relación con el TATC")
    private String estadoTATC = "RETENIDO"; // Por defecto

    @Column(name = "estado_general", nullable = false)
    @Schema(description = "Estado general del contenedor")
    private String estadoGeneral = "REGISTRADO"; // "REGISTRADO" - "EN PATIO" - "DESPACHADO" - "DETENIDO"

}
