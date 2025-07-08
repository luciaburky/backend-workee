package com.example.demo.services.params;

import org.springframework.stereotype.Service;

import com.example.demo.entities.params.ModalidadOferta;
import com.example.demo.repositories.params.ModalidadOfertaRepository;
import com.example.demo.services.BaseServiceImpl;

@Service
public class ModalidadOfertaServiceImpl extends BaseServiceImpl<ModalidadOferta, Long> implements ModalidadOfertaService {

    private final ModalidadOfertaRepository modalidadOfertaRepository;

    public ModalidadOfertaServiceImpl(ModalidadOfertaRepository modalidadOfertaRepository) {
        super(modalidadOfertaRepository);
        this.modalidadOfertaRepository = modalidadOfertaRepository;
    }    
}
