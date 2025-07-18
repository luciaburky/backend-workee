package com.example.demo.services.candidato;

import org.springframework.stereotype.Service;

import com.example.demo.entities.candidato.CandidatoHabilidad;
import com.example.demo.repositories.candidato.CandidatoHabilidadRepository;
import com.example.demo.services.BaseServiceImpl;

@Service
public class CandidatoHabilidadServiceImpl extends BaseServiceImpl<CandidatoHabilidad, Long> implements CandidatoHabilidadService {
    
    private final CandidatoHabilidadRepository candidatoHabilidadRepository;
    
    public CandidatoHabilidadServiceImpl(CandidatoHabilidadRepository candidatoHabilidadRepository) {
        super(candidatoHabilidadRepository);
        this.candidatoHabilidadRepository = candidatoHabilidadRepository;
    }
}
