package com.example.demo.mappers;

import org.mapstruct.*;

import com.example.demo.dtos.OfertaRequestDTO;
import com.example.demo.entities.oferta.Oferta;

@Mapper(componentModel = "spring")
public interface OfertaMapper {
  
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "titulo", source = "titulo")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "responsabilidades", source = "responsabilidades")
    // Iniciales ignorados (se setean en el service)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "modalidadOferta", ignore = true)
    @Mapping(target = "tipoContratoOferta", ignore = true)
    @Mapping(target = "habilidades", ignore = true)
    @Mapping(target = "estadosOferta", ignore = true)
    @Mapping(target = "ofertaEtapas", ignore = true)
    @Mapping(target = "pais", ignore = true)
    @Mapping(target = "finalizadaConExito", ignore = true)
    @Mapping(target = "fechaFinalizacion", ignore = true)
    Oferta toEntity(OfertaRequestDTO dto);
}
