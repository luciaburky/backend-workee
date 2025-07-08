package com.example.demo.entities;

import java.util.Date;

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
@Table(name = "empleado_empresa")
@Getter
@Setter
@Builder
public class EmpleadoEmpresa extends Base {

    @Column(name = "apellido_empleado_empresa")
    @NotNull
    private String apellidoEmpeladoEmpresa;

    @Column(name = "nombre_empleado_empresa")
    @NotNull
    private String nombreEmpleadoEmpresa;

    @Column(name = "puesto_empleado_empresa")
    @NotNull
    private String puestoEmpleadoEmpresa;

    @NotNull
    @Column(name = "fecha_hora_alta_empleado_empresa")
    private Date fechaHoraAltaEmpleadoEmpresa;
    
    @NotNull
    @Column(name = "fecha_hora_baja_empleado_empresa")
    private Date fechaHoraBajaEmpleadoEmpresa;

    @ManyToOne()
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;
}
