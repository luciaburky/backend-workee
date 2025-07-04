package com.example.demo.entities.params;
import jakarta.persistence.Column;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.example.demo.entities.Base;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "estado_usuario")
@Builder
public class EstadoUsuario extends Base {
    @NotNull
    @Column(name = "nombre_estado_usuario")
    private String nombreEstadoUsuario;
 
    @NotNull
    @Column(name = "fecha_estado_usuario")
    private Date fechaHoraAltaEstadoUsuario;
    
    @NotNull
    @Column(name = "fecha_estado_usuario")
    private Date fechaHoraBajaEstadoUsuario;
}
