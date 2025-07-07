package com.example.demo.entities.params;
import jakarta.persistence.Column;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.example.demo.entities.Base;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "tipo_habilidad")
@Builder
public class TipoHabilidad extends Base {
    @NotNull
    @Column(name = "nombre_tipo_habilidad")
    private String nombreTipoHabilidad;
 
    @NotNull
    @Column(name = "fecha_hora_alta_habilidad")
    private Date fechaHoraAltaTipoHabilidad;
    
    @NotNull
    @Column(name = "fecha_hora_baja_habilidad")
    private Date fechaHoraBajaTipoHabilidad;
    
}
