package com.example.demo.services;

import org.springframework.stereotype.Service;

import com.example.demo.entities.params.Habilidad;
import com.example.demo.repositories.HabilidadRepository;
import java.util.List;


@Service
public class HabilidadServiceImpl extends BaseServiceImpl<Habilidad, Long> implements HabilidadService {
    
    private final HabilidadRepository habilidadRepository;
    
    public HabilidadServiceImpl(HabilidadRepository habilidadRepository){
        super(habilidadRepository);
        this.habilidadRepository = habilidadRepository;
    }

    @Override
    public List<Habilidad> findByTipoHabilidad(Long idTipoHabilidad){
        return habilidadRepository.findByTipoHabilidadId(idTipoHabilidad);
    }
}