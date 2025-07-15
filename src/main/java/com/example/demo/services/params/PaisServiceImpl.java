package com.example.demo.services.params;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.params.PaisRequestDTO;
import com.example.demo.entities.params.Pais;
import com.example.demo.exceptions.EntityAlreadyEnabledException;
import com.example.demo.exceptions.EntityAlreadyExistsException;
import com.example.demo.exceptions.EntityReferencedException;
import com.example.demo.repositories.empresa.EmpresaRepository;
import com.example.demo.repositories.params.PaisRepository;
import com.example.demo.services.BaseServiceImpl;

import jakarta.transaction.Transactional;


@Service
public class PaisServiceImpl extends BaseServiceImpl<Pais,Long> implements PaisService{
    private final PaisRepository paisRepository;
    private final EmpresaRepository empresaRepository;


    public PaisServiceImpl(PaisRepository paisRepository, EmpresaRepository empresaRepository) {
        super(paisRepository);
        this.paisRepository = paisRepository;
        this.empresaRepository = empresaRepository;
    }

    @Override
    @Transactional
    public Pais guardarPais(PaisRequestDTO paisRequestDTO) {
        if (yaExistePais(paisRequestDTO.getNombrePais(), null)) {
            throw new EntityAlreadyExistsException("Ya existe un país con ese nombre");
        }

        Pais nuevoPais = new Pais();
        nuevoPais.setNombrePais(paisRequestDTO.getNombrePais());
        nuevoPais.setFechaHoraAlta(new Date());

        return paisRepository.save(nuevoPais);
    }

    @Transactional
    @Override
    public Pais actualizarPais(Long id, PaisRequestDTO paisRequestDTO){
        Pais paisOriginal = this.findById(id); //buscarPaisPorId(id);
        
        if (yaExistePais(paisRequestDTO.getNombrePais(), paisOriginal)) {
            throw new EntityAlreadyExistsException("Ya existe un país con ese nombre");
        }

        paisOriginal.setNombrePais(paisRequestDTO.getNombrePais());
        return paisRepository.save(paisOriginal);
    }

    @Transactional
    @Override
    public Boolean habilitarPais(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("El ID del país no puede ser nulo");
        }
        Pais paisOriginal = this.findById(id); //buscarPaisPorId(id);
        if (paisOriginal.getFechaHoraBaja() == null) {
            throw new EntityAlreadyEnabledException("El país ya está habilitado");
        }
        paisOriginal.setFechaHoraBaja(null);
        paisRepository.save(paisOriginal);
        return true;
    }

    @Override
    public List<Pais> obtenerPaises() {
        return paisRepository.findAllByOrderByNombrePaisAsc();
    }

    @Override
    public List<Pais> obtenerPaisesActivos() {
        return paisRepository.buscarPaisesActivos();
    }


    private Boolean yaExistePais(String nombrePais, Pais paisOriginal) {
        Optional<Pais> paisExistente = paisRepository.findByNombrePaisIgnoreCase(nombrePais); //buscarPorNombrePais(nombrePais);
        
        if(paisOriginal != null && paisExistente.get() != null){
            if(paisOriginal.getId() == paisExistente.get().getId()){
                return false;
            }
        }
        
        return paisExistente.isPresent();
    }

    @Override
    public Boolean deshabilitarPais(Long idPais){
        Boolean estaEnUso = validarUsoPais(idPais);

        if(estaEnUso){
            throw new EntityReferencedException("El pais se encuentra en uso");
        }
        return delete(idPais);
    }

    private Boolean validarUsoPais(Long id){
        Boolean paisEnUsoPorEmpresas = empresaRepository.existenEmpresasActivasUsandoPais(id);
        Boolean paisEnUsoPorCandidato = false; //TODO: Borrar el false y llamar al repo de candidato

        if(paisEnUsoPorCandidato || paisEnUsoPorEmpresas){
            return true;
        } else {
            return false;
        }
    }
}
