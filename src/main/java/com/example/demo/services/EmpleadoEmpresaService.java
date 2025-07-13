package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.EmpleadoEmpresaRequestDTO;
import com.example.demo.entities.EmpleadoEmpresa;

public interface EmpleadoEmpresaService extends BaseService<EmpleadoEmpresa, Long>{
    public EmpleadoEmpresa darDeAltaEmpleado(EmpleadoEmpresaRequestDTO empleadoEmpresaRequestDTO);

    public EmpleadoEmpresa modificarEmpleado(EmpleadoEmpresaRequestDTO empleadoEmpresaRequestDTO, boolean esEmpleadoModificandoseASiMismo, Long id);

    public Boolean darDeBajaEmpleadoEmpresa(Long id);

    public List<EmpleadoEmpresa> visualizarEmpleados(Long idEmpresa);

    public Long contarEmpleadosDeEmpresa(Long idempresa);
}
