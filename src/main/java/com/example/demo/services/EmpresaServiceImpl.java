package com.example.demo.services;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Empresa;
import com.example.demo.repositories.EmpresaRepository;
import com.example.demo.services.params.RubroService;

@Service
public class EmpresaServiceImpl extends BaseServiceImpl<Empresa, Long> implements EmpresaService{

    private final EmpresaRepository empresaRepository;
    private final RubroService rubroService;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository, RubroService rubroService) {
        super(empresaRepository);
        this.empresaRepository = empresaRepository;
        this.rubroService = rubroService;
    }

    
}


