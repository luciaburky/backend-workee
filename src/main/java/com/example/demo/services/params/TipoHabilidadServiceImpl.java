package com.example.demo.services.params;

import org.springframework.stereotype.Service;

import com.example.demo.entities.params.TipoHabilidad;
import com.example.demo.repositories.params.TipoHabilidadRepository;
import com.example.demo.services.BaseServiceImpl;

@Service
public class TipoHabilidadServiceImpl extends BaseServiceImpl<TipoHabilidad, Long> implements TipoHabilidadService{
    
    private final TipoHabilidadRepository tipoHabilidadRepository;
    
    public TipoHabilidadServiceImpl(TipoHabilidadRepository tipoHabilidadRepository){
        super(tipoHabilidadRepository);
        this.tipoHabilidadRepository = tipoHabilidadRepository;
    }
}
