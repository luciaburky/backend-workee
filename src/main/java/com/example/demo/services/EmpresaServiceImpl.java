package com.example.demo.services;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.EmpresaRequestDTO;
import com.example.demo.entities.Empresa;
import com.example.demo.entities.params.Rubro;
import com.example.demo.mappers.EmpresaMapper;
import com.example.demo.repositories.EmpresaRepository;
import com.example.demo.services.params.ProvinciaService;
import com.example.demo.services.params.RubroService;

import jakarta.transaction.Transactional;

@Service
public class EmpresaServiceImpl extends BaseServiceImpl<Empresa, Long> implements EmpresaService{

    private final EmpresaRepository empresaRepository;
    private final RubroService rubroService;
    private final ProvinciaService provinciaService;
    private final EmpresaMapper empresaMapper;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository, RubroService rubroService,ProvinciaService provinciaService, EmpresaMapper empresaMapper) {
        super(empresaRepository);
        this.empresaRepository = empresaRepository;
        this.rubroService = rubroService;
        this.provinciaService = provinciaService;
        this.empresaMapper = empresaMapper;
    }

    @Override
    @Transactional
    public Empresa modificarEmpresa(Long id, EmpresaRequestDTO empresaRequestDTO) {
        if(id == null){
            throw new IllegalArgumentException("El ID de la empresa no puede ser nulo");
        }
        Empresa empresaOriginal = findById(id);
    
        empresaMapper.updateEntityFromDto(empresaRequestDTO, empresaOriginal);
        
        /*if(empresaRequestDTO.getNombreEmpresa() != null && !empresaRequestDTO.getNombreEmpresa().isEmpty()){
            empresaOriginal.setNombreEmpresa(empresaRequestDTO.getNombreEmpresa());
        }
        if(empresaRequestDTO.getDescripcionEmpresa() != null && !empresaRequestDTO.getDescripcionEmpresa().isEmpty()){
            empresaOriginal.setDescripcionEmpresa(empresaRequestDTO.getDescripcionEmpresa());
        }
        if(empresaRequestDTO.getTelefonoEmpresa() != null){
            empresaOriginal.setTelefonoEmpresa(empresaRequestDTO.getTelefonoEmpresa());
        }
        if(empresaRequestDTO.getDireccionEmpresa() != null && !empresaRequestDTO.getDireccionEmpresa().isEmpty()){
            empresaOriginal.setDireccionEmpresa(empresaRequestDTO.getDireccionEmpresa());
        }
        if(empresaRequestDTO.getSitioWebEmpresa() != null && !empresaRequestDTO.getSitioWebEmpresa().isEmpty()){
            empresaOriginal.setSitioWebEmpresa(empresaRequestDTO.getSitioWebEmpresa());
        }*/
        
        if(empresaRequestDTO.getIdRubro() != null && empresaRequestDTO.getIdRubro() != empresaOriginal.getRubro().getId()){
            
            Rubro rubro = rubroService.findById(empresaRequestDTO.getIdRubro());
            
            empresaOriginal.setRubro(rubro);
        }
        //TODO: Falta agregar el campo contraseña pero eso es del módulo de seguridad

        return empresaRepository.save(empresaOriginal);
    }

    
}


