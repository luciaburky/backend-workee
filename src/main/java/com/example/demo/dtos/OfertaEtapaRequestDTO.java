package com.example.demo.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfertaEtapaRequestDTO {

    @NotNull(message = "El numero de la etapa no puede estar vacío")
    private Long numeroEtapa;

    @NotNull(message = "El campo adjunta enlace no puede estar vacío")
    private Boolean adjuntaEnlace;

    @NotNull(message = "El id de la etapa no puede estar vacío")
    private Long idEtapa;

    @NotNull(message = "El id del empleado de la empresa no puede estar vacío")
    private Long idEmpleadoEmpresa;

    private String archivoAdjunto; // Puede ser null si no hay archivo adjunto

    private String descripcionAdicional;
    
}
