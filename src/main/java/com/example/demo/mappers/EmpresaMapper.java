package com.example.demo.mappers;

import org.mapstruct.*;

import com.example.demo.dtos.EmpresaRequestDTO;
import com.example.demo.entities.empresa.Empresa;

@Mapper(componentModel = "spring")
public interface EmpresaMapper {

    // Para creación
    Empresa toEntity(EmpresaRequestDTO dto);

    // Para actualización parcial (omite campos nulos)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(EmpresaRequestDTO dto, @MappingTarget Empresa entity);
}