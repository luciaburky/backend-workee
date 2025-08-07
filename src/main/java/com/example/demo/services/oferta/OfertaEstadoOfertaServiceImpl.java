package com.example.demo.services.oferta;

import org.springframework.stereotype.Service;

import com.example.demo.entities.oferta.OfertaEstadoOferta;
import com.example.demo.repositories.oferta.OfertaEstadoOfertaRepository;
import com.example.demo.services.BaseServiceImpl;

@Service
public class OfertaEstadoOfertaServiceImpl extends BaseServiceImpl<OfertaEstadoOferta, Long> implements OfertaEstadoOfertaService {

    private final OfertaEstadoOfertaRepository ofertaEstadoOfertaRepository;

    public OfertaEstadoOfertaServiceImpl(OfertaEstadoOfertaRepository ofertaEstadoOfertaRepository) {
        super(ofertaEstadoOfertaRepository);
        this.ofertaEstadoOfertaRepository = ofertaEstadoOfertaRepository;
        
    }

    // Implement additional methods specific to OfertaEstadoOferta if needed
    
}
