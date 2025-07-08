package com.example.demo.services.params;

import org.springframework.stereotype.Service;

import com.example.demo.entities.params.EstadoOferta;
import com.example.demo.repositories.params.EstadoOfertaRepository;
import com.example.demo.services.BaseServiceImpl;

@Service
public class EstadoOfertaServiceImpl extends BaseServiceImpl<EstadoOferta, Long> implements EstadoOfertaService {

    private final EstadoOfertaRepository estadoOfertaRepository;

    public EstadoOfertaServiceImpl(EstadoOfertaRepository estadoOfertaRepository) {
        super(estadoOfertaRepository);
        this.estadoOfertaRepository = estadoOfertaRepository;
    }
}
