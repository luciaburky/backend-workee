package com.example.demo.entities.params;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.example.demo.entities.Base;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "tipo_contrato_oferta")
@Builder
public class TipoContratoOferta extends Base {
    @NotNull
    @Column(name = "nombre_tipo_contrato_oferta")
    private String nombreTipoContratoOferta;
    
    @NotNull
    @Column(name = "fecha_hora_alta_tipo_contrato_oferta")
    private Date fechaHoraAltaTipoContratoOferta;
    
    @NotNull
    @Column(name = "fecha_hora_baja_tipo_contrato_oferta")
    private Date fechaHoraBajaTipoContratoOferta;

}