package com.example.demo.services.params;

import org.springframework.stereotype.Service;

import com.example.demo.entities.params.EstadoUsuario;
import com.example.demo.entities.params.Provincia;
import com.example.demo.repositories.params.EstadoUsuarioRepository;
import com.example.demo.services.BaseServiceImpl;

@Service
public class EstadoUsuarioServiceImpl extends BaseServiceImpl<EstadoUsuario,Long> implements EstadoUsuarioService {
    private final EstadoUsuarioRepository estadoUsuarioRepository;

    public EstadoUsuarioServiceImpl(EstadoUsuarioRepository estadoUsuarioRepository) {
        super(estadoUsuarioRepository);
        this.estadoUsuarioRepository = estadoUsuarioRepository;
    }
}
