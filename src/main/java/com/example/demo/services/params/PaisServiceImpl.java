package com.example.demo.services.params;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.params.PaisRequestDTO;
import com.example.demo.entities.params.Pais;
import com.example.demo.exceptions.EntityAlreadyEnabledException;
import com.example.demo.exceptions.EntityAlreadyExistsException;
import com.example.demo.repositories.params.PaisRepository;
import com.example.demo.services.BaseServiceImpl;

import jakarta.transaction.Transactional;


@Service
public class PaisServiceImpl extends BaseServiceImpl<Pais,Long> implements PaisService{
    private final PaisRepository paisRepository;


    public PaisServiceImpl(PaisRepository paisRepository) {
        super(paisRepository);
        this.paisRepository = paisRepository;
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
        Optional<Pais> paisExistente = paisRepository.buscarPorNombrePais(nombrePais);
        
        if(paisOriginal != null){
            if(paisOriginal.getId() == paisExistente.get().getId()){
                return false;
            }
        }
        
        return paisExistente.isPresent();
    }
    
}
