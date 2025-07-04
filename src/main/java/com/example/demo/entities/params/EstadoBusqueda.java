package com.example.demo.entities.params;
import jakarta.persistence.Column;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.example.demo.entities.Base;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "estado_busqueda")
@Builder
public class EstadoBusqueda extends Base {
    @NotNull
    @Column(name = "nombre_estado_busqueda")
    private String nombreEstadoBusqueda;
 
    @NotNull
    @Column(name = "fecha_estado_busqueda")
    private Date fechaHoraAltaEstadoBusqueda;
    
    @NotNull
    @Column(name = "fecha_estado_busqueda")
    private Date fechaHoraBajaEstadoBusqueda;
}
