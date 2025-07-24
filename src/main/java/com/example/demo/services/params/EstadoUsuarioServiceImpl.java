package com.example.demo.services.params;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.params.EstadoUsuarioRequestDTO;
import com.example.demo.entities.params.EstadoUsuario;
import com.example.demo.exceptions.EntityAlreadyEnabledException;
import com.example.demo.exceptions.EntityAlreadyExistsException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.repositories.params.EstadoUsuarioRepository;
import com.example.demo.services.BaseServiceImpl;

import jakarta.transaction.Transactional;

@Service
public class EstadoUsuarioServiceImpl extends BaseServiceImpl<EstadoUsuario,Long> implements EstadoUsuarioService {
    private final EstadoUsuarioRepository estadoUsuarioRepository;

    public EstadoUsuarioServiceImpl(EstadoUsuarioRepository estadoUsuarioRepository) {
        super(estadoUsuarioRepository);
        this.estadoUsuarioRepository = estadoUsuarioRepository;
    }

    @Override
    @Transactional
    public EstadoUsuario guardarEstadoUsuario(EstadoUsuarioRequestDTO estadoUsuarioRequestDTO) {
        if(yaExisteEstadoUsuario(estadoUsuarioRequestDTO.getNombreEstadoUsuario(), null)) {
            throw new EntityAlreadyExistsException("El estado de usuario ya existe");
        }
        EstadoUsuario estadoUsuario = new EstadoUsuario();
        estadoUsuario.setNombreEstadoUsuario(estadoUsuarioRequestDTO.getNombreEstadoUsuario());
        estadoUsuario.setFechaHoraAlta(new Date());
        return estadoUsuarioRepository.save(estadoUsuario);
    }

    @Override
    @Transactional
    public EstadoUsuario actualizarEstadoUsuario(Long id, EstadoUsuarioRequestDTO estadoUsuarioRequestDTO) {
        if(estadoUsuarioRequestDTO.getNombreEstadoUsuario() == null || estadoUsuarioRequestDTO.getNombreEstadoUsuario().isEmpty()) {
            throw new IllegalArgumentException("El nombre del estado de usuario no puede estar vacío");
        }
        EstadoUsuario estadoUsuarioOriginal = findById(id);//buscarEstadoUsuarioPorId(id);
        if(yaExisteEstadoUsuario(estadoUsuarioRequestDTO.getNombreEstadoUsuario(), estadoUsuarioOriginal.getId())) {
            throw new EntityAlreadyExistsException("El estado de usuario ya existe");
        } else {
            estadoUsuarioOriginal.setNombreEstadoUsuario(estadoUsuarioRequestDTO.getNombreEstadoUsuario());
            return estadoUsuarioRepository.save(estadoUsuarioOriginal);
        }
    }

    @Override
    public List<EstadoUsuario> obtenerEstadosUsuario() {
        return estadoUsuarioRepository.findAllByOrderByNombreEstadoUsuarioAsc();
    }

    @Override
    public List<EstadoUsuario> obtenerEstadosUsuarioActivos() {
        return estadoUsuarioRepository.buscarEstadosActivos();
    }

    @Override
    @Transactional
    public Boolean habilitarEstadoUsuario(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        EstadoUsuario estadoUsuario = findById(id);//buscarEstadoUsuarioPorId(id);
        if(estadoUsuario.getFechaHoraBaja() == null) {
            throw new EntityAlreadyEnabledException("El estado de usuario ya está habilitado");
        }
        estadoUsuario.setFechaHoraBaja(null);
        estadoUsuarioRepository.save(estadoUsuario);
        return true;
    }
    

    public Boolean yaExisteEstadoUsuario(String nombreEstadoUsuario, Long idEstadoUsuarioOriginal) {
        Optional<EstadoUsuario> estadoUsuarioExistente = estadoUsuarioRepository.findByNombreEstadoUsuarioIgnoreCase(nombreEstadoUsuario);
        if(idEstadoUsuarioOriginal != null && estadoUsuarioExistente.isPresent()){
            if(idEstadoUsuarioOriginal == estadoUsuarioExistente.get().getId()){
            return false;
            }
        }
        
        return estadoUsuarioExistente.isPresent();
    }

    @Override
    public EstadoUsuario obtenerEstadoPorNombre(String nombreEstado){
        return estadoUsuarioRepository.findByNombreEstadoUsuarioIgnoreCase(nombreEstado)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado un EstadoUsuario para el nombre ingresado"));
    }
}

                
