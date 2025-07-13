package com.example.demo.services;

import com.example.demo.dtos.EmpleadoEmpresaRequestDTO;
import com.example.demo.entities.EmpleadoEmpresa;

public interface EmpleadoEmpresaService extends BaseService<EmpleadoEmpresa, Long>{
    public EmpleadoEmpresa darDeAltaEmpleado(EmpleadoEmpresaRequestDTO empleadoEmpresaRequestDTO);

    public EmpleadoEmpresa modificarEmpleado(EmpleadoEmpresaRequestDTO empleadoEmpresaRequestDTO, boolean esEmpleadoModificandoseASiMismo, Long id);
}
