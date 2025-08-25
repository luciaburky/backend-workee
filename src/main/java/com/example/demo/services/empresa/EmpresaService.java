package com.example.demo.services.empresa;

import java.util.List;

import com.example.demo.dtos.EmpresaRequestDTO;
import com.example.demo.dtos.FiltrosEmpresaRequestDTO;
import com.example.demo.dtos.ResultadoBusquedaEmpresaDTO;
import com.example.demo.entities.empresa.Empresa;
import com.example.demo.services.BaseService;

public interface EmpresaService extends BaseService<Empresa, Long>{
   public Empresa modificarEmpresa(Long id, EmpresaRequestDTO empresaRequestDTO);


   public List<ResultadoBusquedaEmpresaDTO> buscarEmpresasConFiltros(FiltrosEmpresaRequestDTO filtrosEmpresaRequestDTO);

   public List<ResultadoBusquedaEmpresaDTO> buscarEmpresasPorNombre(String nombreEmpresa);
}




 
