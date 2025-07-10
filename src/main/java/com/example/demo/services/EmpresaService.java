package com.example.demo.services;

import com.example.demo.dtos.ModificarEmpresaDTO;
import com.example.demo.entities.Empresa;

public interface EmpresaService extends BaseService<Empresa, Long>{
    public Empresa modificarEmpresa(ModificarEmpresaDTO modificarEmpresaDTO, Long id) throws Exception;
}


