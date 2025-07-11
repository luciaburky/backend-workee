package com.example.demo.services.params;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.params.GeneroRequestDTO;
import com.example.demo.entities.params.Genero;
import com.example.demo.repositories.params.GeneroRepository;
import com.example.demo.services.BaseServiceImpl;
import com.example.exceptions.EntityAlreadyDisabledException;
import com.example.exceptions.EntityAlreadyEnabledException;
import com.example.exceptions.EntityAlreadyExistsException;
import com.example.exceptions.EntityNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class GeneroServiceImpl extends BaseServiceImpl<Genero,Long> implements GeneroService {

    private final GeneroRepository generoRepository;

    public GeneroServiceImpl(GeneroRepository generoRepository) {
        super(generoRepository);
        this.generoRepository = generoRepository;
    }

    @Override
    @Transactional
    public Genero guardarGenero(GeneroRequestDTO generoRequestDTO) {
        if(yaExisteEstadoUsuario(generoRequestDTO.getNombreGenero())) {
            throw new EntityAlreadyExistsException("El género ya existe");
        }
        Genero genero = new Genero();
        genero.setNombreGenero(generoRequestDTO.getNombreGenero());
        genero.setFechaHoraAlta(new Date());
        return generoRepository.save(genero);
    }

    @Override
    @Transactional
    public Genero actualizarGenero(Long id, GeneroRequestDTO generoRequestDTO) {
        if(generoRequestDTO.getNombreGenero() == null || generoRequestDTO.getNombreGenero().isEmpty()) {
            throw new IllegalArgumentException("El nombre del género no puede estar vacío");
        }
        Genero genero = buscarGeneroPorId(id);
        if(yaExisteEstadoUsuario(generoRequestDTO.getNombreGenero())) {
            throw new EntityAlreadyExistsException("El género ya existe");
        } else {
            genero.setNombreGenero(generoRequestDTO.getNombreGenero());
            return generoRepository.save(genero);
        }
    }

    @Override
    public Genero buscarGeneroPorId(Long id) {
        return generoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genero no encontrado con ID: " + id));
    }

    @Override
    public List<Genero> obtenerGeneros() {
        return generoRepository.findAllByOrderByNombreGeneroAsc();
    }

    @Override
    public List<Genero> obtenerGenerosActivos() {
        return generoRepository.buscarGenerosActivos();
    }

    @Override
    @Transactional
    public Boolean habilitarGenero(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        Genero genero = buscarGeneroPorId(id);
        if(genero.getFechaHoraBaja() == null) {
            throw new EntityAlreadyEnabledException("El género ya está habilitado");
        }
        genero.setFechaHoraBaja(null);
        generoRepository.save(genero);
        return true;
    }

    @Override
    @Transactional
    public Boolean deshabilitarGenero(Long id) {
       if(id == null){
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        Genero genero = buscarGeneroPorId(id);
        if(genero.getFechaHoraBaja() != null) {
            throw new EntityAlreadyDisabledException("El genero ya está deshabilitado");
        }
        genero.setFechaHoraBaja(new Date());
        generoRepository.save(genero);
        return true;
    } 
    

    private Boolean yaExisteEstadoUsuario(String nombreEstadoUsuario) {
        return generoRepository.findByNombreGeneroIgnoreCase(nombreEstadoUsuario).isPresent();
    }

}
