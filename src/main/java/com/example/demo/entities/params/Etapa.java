package com.example.demo.entities.params;

import jakarta.persistence.Column;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.example.demo.entities.Base;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "etapa")
@Builder
public class Etapa extends Base {
    @NotNull
    @Column(name = "nombre_etapa")
    private String nombreEtapa;
    
    @NotNull
    @Column(name = "descripcion_etapa")
    private String descripcionEtapa;
    
    @NotNull
    @Column(name = "fecha_etapa")
    private Date fechaHoraAltaEtapa;
    
    @NotNull
    @Column(name = "fecha_etapa")
    private Date fechaHoraBajaEtapa;

}

