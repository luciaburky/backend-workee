package com.example.demo.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.demo.dtos.EmpleadoEmpresaRequestDTO;
import com.example.demo.entities.EmpleadoEmpresa;

@Mapper(componentModel = "spring")
public interface EmpleadoEmpresaMapper {
    
    //Para creacion
    EmpleadoEmpresa toEntity(EmpleadoEmpresaRequestDTO dto);

    //Para modificacion
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    //@Mapping(target = "correoEmpleadoEmpresa", ignore = true)
    void updateEntityFromDto(EmpleadoEmpresaRequestDTO dto, @MappingTarget EmpleadoEmpresa entity);
    
}
