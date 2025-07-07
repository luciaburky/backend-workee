package com.example.demo.services.params;

import org.springframework.stereotype.Service;

import com.example.demo.entities.params.Provincia;
import com.example.demo.entities.params.TipoEvento;
import com.example.demo.repositories.params.TipoEventoRepository;
import com.example.demo.services.BaseServiceImpl;

@Service
public class TipoEventoServiceImpl extends BaseServiceImpl<TipoEvento,Long> implements TipoEventoService {
    private TipoEventoRepository tipoEventoRepository;

    public TipoEventoServiceImpl(TipoEventoRepository tipoEventoRepository) {
        super(tipoEventoRepository);
        this.tipoEventoRepository = tipoEventoRepository;
    }
}
