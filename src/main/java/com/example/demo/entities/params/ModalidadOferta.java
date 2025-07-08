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
@Table(name = "modalidad_oferta")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModalidadOferta extends Base {
    @NotNull
    @Column(name = "nombre_modalidad_oferta")
    private String nombreModalidadOferta;
    
    @NotNull
    @Column(name = "fecha_hora_alta_modalidad_oferta")
    private Date fechaHoraAltaModalidadOferta;
    
    @NotNull
    @Column(name = "fecha_hora_baja_modalidad_oferta")
    private Date fechaHoraBajaModalidadOferta;

}
