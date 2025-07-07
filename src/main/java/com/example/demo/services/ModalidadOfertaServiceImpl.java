package com.example.demo.services;

import org.springframework.stereotype.Service;

import com.example.demo.entities.params.ModalidadOferta;
import com.example.demo.repositories.ModalidadOfertaRepository;

@Service
public class ModalidadOfertaServiceImpl extends BaseServiceImpl<ModalidadOferta, Long> implements ModalidadOfertaService {

    private final ModalidadOfertaRepository modalidadOfertaRepository;

    public ModalidadOfertaServiceImpl(ModalidadOfertaRepository modalidadOfertaRepository) {
        super(modalidadOfertaRepository);
        this.modalidadOfertaRepository = modalidadOfertaRepository;
    }    
}
