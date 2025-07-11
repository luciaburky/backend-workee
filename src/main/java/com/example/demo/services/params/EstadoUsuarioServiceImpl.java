package com.example.demo.services.params;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.params.EstadoUsuarioRequestDTO;
import com.example.demo.entities.params.EstadoUsuario;
import com.example.demo.exceptions.EntityAlreadyDisabledException;
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
        if(yaExisteEstadoUsuario(estadoUsuarioRequestDTO.getNombreEstadoUsuario())) {
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
        EstadoUsuario estadoUsuario = buscarEstadoUsuarioPorId(id);
        if(yaExisteEstadoUsuario(estadoUsuarioRequestDTO.getNombreEstadoUsuario())) {
            throw new EntityAlreadyExistsException("El estado de usuario ya existe");
        } else {
            estadoUsuario.setNombreEstadoUsuario(estadoUsuarioRequestDTO.getNombreEstadoUsuario());
            return estadoUsuarioRepository.save(estadoUsuario);
        }
    }

    @Override
    public EstadoUsuario buscarEstadoUsuarioPorId(Long id) {
        return estadoUsuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estado de usuario no encontrado con ID: " + id));
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
        EstadoUsuario estadoUsuario = buscarEstadoUsuarioPorId(id);
        if(estadoUsuario.getFechaHoraBaja() == null) {
            throw new EntityAlreadyEnabledException("El estado de usuario ya está habilitado");
        }
        estadoUsuario.setFechaHoraBaja(null);
        estadoUsuarioRepository.save(estadoUsuario);
        return true;
    }

    @Override
    @Transactional
    public Boolean deshabilitarEstadoUsuario(Long id) {
       if(id == null){
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        EstadoUsuario estadoUsuario = buscarEstadoUsuarioPorId(id);
        if(estadoUsuario.getFechaHoraBaja() != null) {
            throw new EntityAlreadyDisabledException("El estado de usuario ya está deshabilitado");
        }
        estadoUsuario.setFechaHoraBaja(new Date());
        estadoUsuarioRepository.save(estadoUsuario);
        return true;
    } 
    

    public Boolean yaExisteEstadoUsuario(String nombreEstadoUsuario) {
        return estadoUsuarioRepository.findByNombreEstadoUsuarioIgnoreCase(nombreEstadoUsuario).isPresent();
    }
}
