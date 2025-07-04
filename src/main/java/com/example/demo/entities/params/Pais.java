package com.example.demo.entities.params;
import jakarta.persistence.Column;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.example.demo.entities.Base;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "pais")
@Builder
public class Pais extends Base {
    @NotNull
    @Column(name = "nombre_pais")
    private String nombrePais;
 
    @NotNull
    @Column(name = "fecha_pais")
    private Date fechaHoraAltaPais;
    
    @NotNull
    @Column(name = "fecha_pais")
    private Date fechaHoraBajaPais;

}