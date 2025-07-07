package com.example.demo.services.params;

import org.springframework.stereotype.Service;

import com.example.demo.entities.params.TipoContratoOferta;
import com.example.demo.repositories.params.TipoContratoOfertaRepository;
import com.example.demo.services.BaseServiceImpl;

@Service
public class TipoContratoOfertaServiceImpl extends BaseServiceImpl<TipoContratoOferta,Long> implements TipoContratoOfertaService {

    private TipoContratoOfertaRepository tipoContratoOfertaRepository;

    public TipoContratoOfertaServiceImpl(TipoContratoOfertaRepository tipoContratoOfertaRepository) {
        super(tipoContratoOfertaRepository);
        this.tipoContratoOfertaRepository = tipoContratoOfertaRepository;
    }

}
