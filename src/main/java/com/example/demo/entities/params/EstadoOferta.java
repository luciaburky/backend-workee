package com.example.demo.entities.params;

import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.example.demo.entities.Base;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "estado_oferta")
@Builder
@Getter
@Setter
public class EstadoOferta extends Base {
    @NotNull
    @Column(name = "nombre_estado_oferta")
    private String nombreEstadoOferta;

}