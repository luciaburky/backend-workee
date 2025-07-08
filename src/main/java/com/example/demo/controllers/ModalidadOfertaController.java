package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.params.ModalidadOferta;
import com.example.demo.services.ModalidadOfertaService;

@RestController
@RequestMapping("/modalidades-oferta")
public class ModalidadOfertaController extends BaseControllerImpl<ModalidadOferta, ModalidadOfertaService> {

    private final ModalidadOfertaService modalidadOfertaService;

    public ModalidadOfertaController(ModalidadOfertaService modalidadOfertaService) {
        super(modalidadOfertaService);
        this.modalidadOfertaService = modalidadOfertaService;
    }
    
}
