package com.example.demo.services;

import org.springframework.stereotype.Service;

import com.example.demo.entities.params.EstadoOferta;
import com.example.demo.repositories.EstadoOfertaRepository;

@Service
public class EstadoOfertaServiceImpl extends BaseServiceImpl<EstadoOferta, Long> implements EstadoOfertaService {

    private final EstadoOfertaRepository estadoOfertaRepository;

    public EstadoOfertaServiceImpl(EstadoOfertaRepository estadoOfertaRepository) {
        super(estadoOfertaRepository);
        this.estadoOfertaRepository = estadoOfertaRepository;
    }
}
