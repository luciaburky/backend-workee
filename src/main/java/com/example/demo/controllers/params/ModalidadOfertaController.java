package com.example.demo.controllers.params;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controllers.BaseControllerImpl;
import com.example.demo.entities.params.ModalidadOferta;
import com.example.demo.services.params.ModalidadOfertaService;

@RestController
@RequestMapping("/modalidades-oferta")
public class ModalidadOfertaController extends BaseControllerImpl<ModalidadOferta, ModalidadOfertaService> {

    private final ModalidadOfertaService modalidadOfertaService;

    public ModalidadOfertaController(ModalidadOfertaService modalidadOfertaService) {
        super(modalidadOfertaService);
        this.modalidadOfertaService = modalidadOfertaService;
    }
    
}
