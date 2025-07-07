package com.example.demo.entities.params;
import jakarta.persistence.Column;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.example.demo.entities.Base;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "habilidad")
@Builder
public class Habilidad extends Base {
    @NotNull
    @Column(name = "nombre_habilidad")
    private String nombreHabilidad;
 
    @NotNull
    @Column(name = "fecha_hora_alta_habilidad")
    private Date fechaHoraAltaHabilidad;
    
    @NotNull
    @Column(name = "fecha_hora_baja_habilidad")
    private Date fechaHoraBajaHabilidad;
    
    @ManyToOne
    @JoinColumn(name = "tipo_habilidad_id", nullable = false)
    private TipoHabilidad tipoHabilidad;
}
