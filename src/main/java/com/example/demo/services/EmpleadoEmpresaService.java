package com.example.demo.services;

import com.example.demo.dtos.CrearEmpleadoEmpresaDTO;
import com.example.demo.dtos.ModificarEmpleadoEmpresaDTO;
import com.example.demo.entities.EmpleadoEmpresa;

public interface EmpleadoEmpresaService extends BaseService<EmpleadoEmpresa, Long>{
    public EmpleadoEmpresa crearEmpleadoEmpresa(CrearEmpleadoEmpresaDTO empleadoEmpresaRequestDTO) throws Exception;
    public EmpleadoEmpresa actualizarEmpleado(ModificarEmpleadoEmpresaDTO modificarEmpleadoEmpresaDTO, Long idEmpleadoEmpresa) throws Exception;

    public Long contarEmpleadosActivos(Long idEmpresa) throws Exception;
    public Boolean darDeBajaEmpleado(Long idEmpleadoEmpresa) throws Exception;
}
