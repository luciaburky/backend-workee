package com.example.demo.entities;

import java.util.Date;

import com.example.demo.entities.params.Pais;
import com.example.demo.entities.params.Provincia;
import com.example.demo.entities.params.Rubro;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "empresa")
@Getter
@Setter
@Builder
public class Empresa extends Base{
 
    @NotNull
    @Column(name = "nombre_empresa")
    private String nombreEmpresa;

    @NotNull
    @Column(name = "descripcion_empresa")
    private String descripcionEmpresa;

    @NotNull
    @Column(name = "numero_identificacion_fiscal")
    private String numeroIdentificacionFiscal;

    @NotNull
    @Column(name = "telefono_empresa")
    private Integer telefonoEmpresa;

    @NotNull
    @Column(name = "email_empresa")
    private String emailEmpresa;

    @NotNull
    @Column(name = "direccion_empresa")
    private String direccionEmpresa;

    @NotNull
    @Column(name = "sitio_web_empresa")
    private String sitioWebEmpresa;

    @NotNull
    @Column(name = "fecha_hora_alta_empresa")
    private Date fechaHoraAltaEmpresa;
    
    @NotNull
    @Column(name = "fecha_hora_baja_empresa")
    private Date fechaHoraBajaEmpresa;

    @ManyToOne()
    @JoinColumn(name = "id_provincia")
    private Provincia provincia;

    @ManyToOne()
    @JoinColumn(name = "id_rubro")
    @NotNull
    private Rubro rubro;
}
