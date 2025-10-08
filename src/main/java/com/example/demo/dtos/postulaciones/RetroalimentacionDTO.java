package com.example.demo.dtos.postulaciones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetroalimentacionDTO {
    private Long idPostulacion;
    private Long idPostulacionOfertaEtapa;
    private String retroalimentacion;
}
