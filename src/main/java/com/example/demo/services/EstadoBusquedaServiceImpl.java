package com.example.demo.services;

import org.springframework.stereotype.Service;

import com.example.demo.entities.params.EstadoBusqueda;
import com.example.demo.repositories.EstadoBusquedaRepository;

@Service
public class EstadoBusquedaServiceImpl extends BaseServiceImpl<EstadoBusqueda, Long> implements EstadoBusquedaService {
    
    private final EstadoBusquedaRepository estadoBusquedaRepository;
   
    public EstadoBusquedaServiceImpl(EstadoBusquedaRepository estadoBusquedaRepository) {
        super(estadoBusquedaRepository);
        this.estadoBusquedaRepository = estadoBusquedaRepository;
    }
}
