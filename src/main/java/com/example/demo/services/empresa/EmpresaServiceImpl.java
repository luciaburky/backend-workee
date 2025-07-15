package com.example.demo.services.empresa;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.EmpresaRequestDTO;
import com.example.demo.dtos.FiltrosEmpresaRequestDTO;
import com.example.demo.entities.empresa.Empresa;
import com.example.demo.entities.params.Rubro;
import com.example.demo.mappers.EmpresaMapper;
import com.example.demo.repositories.empresa.EmpresaRepository;
import com.example.demo.services.BaseServiceImpl;
import com.example.demo.services.params.RubroService;

import jakarta.transaction.Transactional;

@Service
public class EmpresaServiceImpl extends BaseServiceImpl<Empresa, Long> implements EmpresaService{

    private final EmpresaRepository empresaRepository;
    private final RubroService rubroService;
    private final EmpresaMapper empresaMapper;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository, RubroService rubroService, EmpresaMapper empresaMapper) {
        super(empresaRepository);
        this.empresaRepository = empresaRepository;
        this.rubroService = rubroService;
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
        
        if(empresaRequestDTO.getIdRubro() != null && empresaRequestDTO.getIdRubro() != empresaOriginal.getRubro().getId()){
            Rubro rubro = rubroService.findById(empresaRequestDTO.getIdRubro());
            empresaOriginal.setRubro(rubro);
        }
        //TODO: Falta agregar el campo contraseña pero eso es del módulo de seguridad

        return empresaRepository.save(empresaOriginal);
    }

    @Override
    public List<Empresa> buscarEmpresasConFiltros(FiltrosEmpresaRequestDTO filtrosEmpresaRequestDTO){
        return empresaRepository.buscarEmpresasConFiltros(filtrosEmpresaRequestDTO.getIdsRubros(), filtrosEmpresaRequestDTO.getIdsProvincias());
    }
    
}


