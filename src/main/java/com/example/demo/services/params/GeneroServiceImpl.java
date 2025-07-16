package com.example.demo.services.params;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.params.GeneroRequestDTO;
import com.example.demo.entities.params.Genero;
import com.example.demo.exceptions.EntityAlreadyEnabledException;
import com.example.demo.exceptions.EntityAlreadyExistsException;
import com.example.demo.repositories.params.GeneroRepository;
import com.example.demo.services.BaseServiceImpl;

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
        if(yaExisteEstadoUsuario(generoRequestDTO.getNombreGenero(), null)) {
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
        Genero generoOriginal = findById(id);//buscarGeneroPorId(id);
        if(yaExisteEstadoUsuario(generoRequestDTO.getNombreGenero(), generoOriginal.getId())) {
            throw new EntityAlreadyExistsException("El género ya existe");
        } else {
            generoOriginal.setNombreGenero(generoRequestDTO.getNombreGenero());
            return generoRepository.save(generoOriginal);
        }
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
        Genero genero = findById(id);//buscarGeneroPorId(id);
        if(genero.getFechaHoraBaja() == null) {
            throw new EntityAlreadyEnabledException("El género ya está habilitado");
        }
        genero.setFechaHoraBaja(null);
        generoRepository.save(genero);
        return true;
    }
    

    private Boolean yaExisteEstadoUsuario(String nombreEstadoUsuario, Long idGeneroOriginal) {
        Optional<Genero> generoExistente = generoRepository.findByNombreGeneroIgnoreCase(nombreEstadoUsuario);
        if(idGeneroOriginal != null && generoExistente.isPresent()){
            if(idGeneroOriginal == generoExistente.get().getId()){
                return false;
            }
        }
        return generoExistente.isPresent();
    }

}
