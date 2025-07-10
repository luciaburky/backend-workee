package com.example.demo.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModificarEmpresaDTO {
    
    private String nombreEmpresa;

    private String descripcionEmpresa;

    private Integer telefonoEmpresa;

    private String direccionEmpresa;

    private String sitioWebEmpresa;

    private Long idRubro;
}
