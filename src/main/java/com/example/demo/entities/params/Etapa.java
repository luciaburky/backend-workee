package com.example.demo.entities.params;

import jakarta.persistence.Column;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.example.demo.entities.Base;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "etapa")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Etapa extends Base {
    @NotNull
    @Column(name = "nombre_etapa")
    private String nombreEtapa;
    
    @NotNull
    @Column(name = "descripcion_etapa")
    private String descripcionEtapa;
}

