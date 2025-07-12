package com.example.demo.mappers;

import org.mapstruct.Mapper;

import com.example.demo.dtos.EmpleadoEmpresaRequestDTO;
import com.example.demo.entities.EmpleadoEmpresa;

@Mapper(componentModel = "spring")
public interface EmpleadoEmpresaMapper {
    
    //Para creacion
    EmpleadoEmpresa toEntity(EmpleadoEmpresaRequestDTO dto);

    
}
