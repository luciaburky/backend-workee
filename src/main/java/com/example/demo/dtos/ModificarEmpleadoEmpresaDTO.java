package com.example.demo.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModificarEmpleadoEmpresaDTO {

    private String nombreEmpleadoEmpresa;
    
    private String apellidoEmpleadoEmpresa;
    
    private String puestoEmpleadoEmpresa;
}
