package com.example.demo.services;

import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.ModificarEmpresaDTO;
import com.example.demo.entities.Empresa;
import com.example.demo.entities.params.Rubro;
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


