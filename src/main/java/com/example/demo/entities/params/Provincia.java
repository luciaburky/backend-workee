package com.example.demo.entities.params;
import jakarta.persistence.Column;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.example.demo.entities.Base;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "provincia")
@Builder
public class Provincia extends Base {
    @NotNull
    @Column(name = "nombre_provincia")
    private String nombreProvincia;
 
    @NotNull
    @Column(name = "fecha_provincia")
    private Date fechaHoraAltaProvincia;
    
    @NotNull
    @Column(name = "fecha_provincia")
    private Date fechaHoraBajaProvincia;

}