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
import com.example.demo.services.seguridad.UsuarioService;

import jakarta.transaction.Transactional;

@Service
public class EmpresaServiceImpl extends BaseServiceImpl<Empresa, Long> implements EmpresaService{

    private final EmpresaRepository empresaRepository;
    private final RubroService rubroService;
    private final EmpresaMapper empresaMapper;
    private final UsuarioService usuarioService;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository, RubroService rubroService, EmpresaMapper empresaMapper, UsuarioService usuarioService) {
        super(empresaRepository);
        this.empresaRepository = empresaRepository;
        this.rubroService = rubroService;
        this.empresaMapper = empresaMapper;
        this.usuarioService = usuarioService;
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
        
        usuarioService.actualizarDatosUsuario(empresaOriginal.getUsuario().getId(), empresaRequestDTO.getContrasenia(), empresaRequestDTO.getRepetirContrasenia(), empresaRequestDTO.getUrlFotoPerfil());

        return empresaRepository.save(empresaOriginal);
    }

    @Override
    public List<Empresa> buscarEmpresasConFiltros(FiltrosEmpresaRequestDTO filtrosEmpresaRequestDTO){
        return empresaRepository.buscarEmpresasConFiltros(filtrosEmpresaRequestDTO.getIdsRubros(), filtrosEmpresaRequestDTO.getIdsProvincias());
    }

    @Override
    public List<Empresa> buscarEmpresasPorNombre(String nombreEmpresa){
        if(nombreEmpresa.isEmpty() || nombreEmpresa == null){
            throw new IllegalArgumentException("El nombre de la empresa no puede estar vacío");
        }
        return empresaRepository.buscarEmpresasPorNombre(nombreEmpresa);
    }
    
}


