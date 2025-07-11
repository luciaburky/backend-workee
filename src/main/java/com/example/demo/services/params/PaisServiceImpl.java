package com.example.demo.services.params;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.params.PaisRequestDTO;
import com.example.demo.entities.params.Pais;
import com.example.demo.entities.params.Provincia;
import com.example.demo.repositories.params.PaisRepository;
import com.example.demo.services.BaseServiceImpl;
import com.example.exceptions.EntityAlreadyDisabled;
import com.example.exceptions.EntityAlreadyEnabled;
import com.example.exceptions.EntityAlreadyExistsException;
import com.example.exceptions.EntityNotFoundException;

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
        if (yaExistePais(paisRequestDTO.getNombrePais())) {
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
        Pais paisOriginal = buscarPaisPorId(id);
        
        if (yaExistePais(paisRequestDTO.getNombrePais())) {
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
        Pais paisOriginal = buscarPaisPorId(id);
        if (paisOriginal.getFechaHoraBaja() == null) {
            throw new EntityAlreadyEnabled("El país ya está habilitado");
        }
        paisOriginal.setFechaHoraBaja(null);
        paisRepository.save(paisOriginal);
        return true;
    }

    @Override
    @Transactional
    public Boolean deshabilitarPais(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("El ID del país no puede ser nulo");
        }
        Pais paisOriginal = buscarPaisPorId(id);
        if (paisOriginal.getFechaHoraBaja() != null) {
            throw new EntityAlreadyDisabled("El país ya está deshabilitado");
        }
        paisOriginal.setFechaHoraBaja(new Date());
        paisRepository.save(paisOriginal);
        return true;
    }

    @Override
    public Pais buscarPaisPorId(Long id) {
        return paisRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("País no encontrado con ID: " + id));
    }

    @Override
    public List<Pais> obtenerPaises() {
        return paisRepository.findAllByOrderByNombrePaisAsc();
    }

    @Override
    public List<Pais> obtenerPaisesActivos() {
        return paisRepository.buscarPaisesActivos();
    }


    private Boolean yaExistePais(String nombrePais) {
        Optional<Pais> paisExistente = paisRepository.findByNombrePaisIgnoreCase(nombrePais);
        return paisExistente.isPresent();
    }
    
}
