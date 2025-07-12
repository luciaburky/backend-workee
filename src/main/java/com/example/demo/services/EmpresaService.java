package com.example.demo.services;

import com.example.demo.dtos.EmpresaRequestDTO;
import com.example.demo.entities.Empresa;

public interface EmpresaService extends BaseService<Empresa, Long>{
   public Empresa modificarEmpresa(Long id, EmpresaRequestDTO empresaRequestDTO);
}


