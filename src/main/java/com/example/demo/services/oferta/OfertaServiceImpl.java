package com.example.demo.services.oferta;

import com.example.demo.entities.oferta.Oferta;
import com.example.demo.repositories.oferta.OfertaRepository;
import com.example.demo.services.BaseServiceImpl;

public class OfertaServiceImpl extends BaseServiceImpl<Oferta, Long> implements OfertaService {

    private final OfertaRepository ofertaRepository;

    public OfertaServiceImpl(OfertaRepository ofertaRepository) {
        super(ofertaRepository);
        this.ofertaRepository = ofertaRepository;
    }
}
