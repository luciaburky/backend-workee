package com.example.demo.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaPendienteHabilitacionDTO {
    private Long idEmpresa;
    private String nombreEmpresa;
    private String logoEmpresa;
    private String correoEmpresa;
    private Date fechaHoraRegistroEmpresa;
}
