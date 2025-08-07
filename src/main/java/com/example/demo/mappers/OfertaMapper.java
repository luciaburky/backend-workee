package com.example.demo.mappers;

import org.mapstruct.*;

import com.example.demo.dtos.OfertaRequestDTO;
import com.example.demo.entities.oferta.Oferta;

@Mapper(componentModel = "spring")
public interface OfertaMapper {
    //Para creacion
    Oferta toEntity(OfertaRequestDTO dto);

} 
