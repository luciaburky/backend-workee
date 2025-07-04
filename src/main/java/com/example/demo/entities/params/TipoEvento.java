package com.example.demo.entities.params;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.example.demo.entities.Base;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "tipo_evento")
@Builder
public class TipoEvento extends Base {
    @NotNull
    @Column(name = "nombre_tipo_evento")
    private String nombreTipoEvento;
    
    @NotNull
    @Column(name = "fecha_hora_alta_tipo_evento")
    private Date fechaHoraAltaTipoEvento;
    
    @NotNull
    @Column(name = "fecha_hora_baja_tipo_evento")
    private Date fechaHoraBajaTipoEvento;

}
