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

    public Empresa modificarEmpresa(ModificarEmpresaDTO modificarEmpresaDTO, Long idEmpresa) throws Exception{
        try{
            Empresa empresaOriginal = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new NotFoundException());

            if(modificarEmpresaDTO.getNombreEmpresa() != null) {
                empresaOriginal.setNombreEmpresa(modificarEmpresaDTO.getNombreEmpresa());
            }
            if(modificarEmpresaDTO.getDescripcionEmpresa() != null) {
                empresaOriginal.setDescripcionEmpresa(modificarEmpresaDTO.getDescripcionEmpresa());
            }
            if(modificarEmpresaDTO.getTelefonoEmpresa() != null) {
                empresaOriginal.setTelefonoEmpresa(modificarEmpresaDTO.getTelefonoEmpresa());
            }
            if(modificarEmpresaDTO.getDireccionEmpresa() != null) {
                empresaOriginal.setDireccionEmpresa(modificarEmpresaDTO.getDireccionEmpresa());
            }
            if(modificarEmpresaDTO.getIdRubro() != null) {
                Rubro rubroEmpresa = rubroService.findById(modificarEmpresaDTO.getIdRubro());
                if(rubroEmpresa != null) {
                    empresaOriginal.setRubro(rubroEmpresa);
                } else {
                    throw new NotFoundException();
                }
            }
            return empresaRepository.save(empresaOriginal);
            
        } catch (DataAccessException e) {
            throw new Exception("Error de base de datos al modificar la empresa: " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Error inesperado al modificar la empresa: " + e.getMessage());
        }
        
        
    }
}


