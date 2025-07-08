package com.example.demo.entities.params;

import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.example.demo.entities.Base;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "genero")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Genero extends Base {
    @NotNull
    @Column(name = "nombre_genero")
    private String nombreGenero;
    
    @NotNull
    @Column(name = "fecha_hora_alta_genero")
    private Date fechaHoraAltaGenero;
    
    @NotNull
    @Column(name = "fecha_hora_baja_genero")
    private Date fechaHoraBajaGenero;

}
