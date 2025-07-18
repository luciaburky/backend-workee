package com.example.demo.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.demo.dtos.CandidatoRequestDTO;
import com.example.demo.entities.candidato.Candidato;


@Mapper(componentModel = "spring")
public interface CandidatoMapper {

    //Creacion 
    Candidato toEntity(CandidatoRequestDTO dto);

    //Actualizacion / Actualizacion parcial
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Candidato updateEntityFromDto(CandidatoRequestDTO dto, @MappingTarget Candidato entity);
}
