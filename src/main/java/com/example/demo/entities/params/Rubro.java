package com.example.demo.entities.params;
import jakarta.persistence.Column;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.example.demo.entities.Base;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "rubro")
@Builder
public class Rubro extends Base {
    @NotNull
    @Column(name = "nombre_rubro")
    private String nombreRubro;
 
    @NotNull
    @Column(name = "fecha_rubro")
    private Date fechaHoraAltaRubro;
    
    @NotNull
    @Column(name = "fecha_rubro")
    private Date fechaHoraBajaRubro;
    
}
