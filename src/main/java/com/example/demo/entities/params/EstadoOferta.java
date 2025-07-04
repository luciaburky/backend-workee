package com.example.demo.entities.params;

import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.example.demo.entities.Base;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "estado_oferta")
@Builder
public class EstadoOferta extends Base {
    @NotNull
    @Column(name = "nombre_estado_oferta")
    private String nombreEstadoOferta;
    
    @NotNull
    @Column(name = "fecha_hora_alta_estado_oferta")
    private Date fechaHoraAltaEstadoOferta;
    
    @NotNull
    @Column(name = "fecha_hora_baja_estado_oferta")
    private Date fechaHoraBajaEstadoOferta;

}