package com.example.demo.dtos;

import java.util.List;

import com.example.demo.entities.oferta.Oferta;
import com.example.demo.entities.oferta.OfertaEtapa;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfertaRequestDTO {
    
    @NotBlank(message = "El título de la oferta no puede estar vacío")
    private String titulo;
    
    @NotBlank(message = "La descripción de la oferta no puede estar vacía")
    private String descripcion;   

    @NotBlank(message = "Las responsabilidades de la oferta no pueden estar vacías")
    private String responsabilidades;

    @NotNull(message = "El id de la empresa no puede estar vacío")
    private Long idEmpresa;

    @NotNull(message = "El id de la modalidad de oferta no puede estar vacío")
    private Long idModalidadOferta;

    @NotNull(message = "El id del tipo de contrato de oferta no puede estar vacío")
    private Long idTipoContratoOferta;

    private List<Long> idHabilidades;

    @NotEmpty(message = "La lista de etapas de la oferta no puede estar vacía")
    private List<@Valid OfertaEtapaRequestDTO> ofertaEtapas;

    /*
    Nota: Se setean solos: 
    - Estado (y EstadoOfertaEstado) como "Abierto",
    - fechaFinalizacion como null,
    - finalizadaConExito como null 
    */
}
