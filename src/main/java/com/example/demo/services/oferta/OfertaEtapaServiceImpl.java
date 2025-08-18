package com.example.demo.services.oferta;

import org.springframework.stereotype.Service;

import com.example.demo.entities.oferta.OfertaEtapa;
import com.example.demo.repositories.oferta.OfertaEtapaRepository;
import com.example.demo.services.BaseServiceImpl;

@Service
public class OfertaEtapaServiceImpl extends BaseServiceImpl<OfertaEtapa, Long> implements OfertaEtapaService {
    
    private final OfertaEtapaRepository ofertaEtapaRepository;

    public OfertaEtapaServiceImpl(OfertaEtapaRepository ofertaEtapaRepository) {
        super(ofertaEtapaRepository);
        this.ofertaEtapaRepository = ofertaEtapaRepository;
    }


}
