package com.example.demo.services;


import org.springframework.stereotype.Service;

import com.example.demo.entities.EmpleadoEmpresa;
import com.example.demo.repositories.EmpleadoEmpresaRepository;

@Service
public class EmpleadoEmpresaServiceImpl extends BaseServiceImpl<EmpleadoEmpresa, Long> implements EmpleadoEmpresaService{
    private final EmpleadoEmpresaRepository empleadoEmpresaRepository;

    private final EmpresaService empresaService;

    public EmpleadoEmpresaServiceImpl(EmpleadoEmpresaRepository empleadoEmpresaRepository, EmpresaService empresaService) {
        super(empleadoEmpresaRepository);
        this.empleadoEmpresaRepository = empleadoEmpresaRepository;
        this.empresaService = empresaService;
    }
 
   
}


