package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.params.TipoContratoOferta;
import com.example.demo.services.params.TipoContratoOfertaService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/tipos-contrato-oferta")
@Tag(name = "TipoContratoOferta", description = "Controlador para operaciones CRUD de TipoContratoOferta")
public class TipoContratoOfertaController extends BaseControllerImpl<TipoContratoOferta, TipoContratoOfertaService> {

    private final TipoContratoOfertaService tipoContratoOfertaService;

    public TipoContratoOfertaController(TipoContratoOfertaService tipoContratoOfertaService) {
        super(tipoContratoOfertaService);
        this.tipoContratoOfertaService = tipoContratoOfertaService;
    }

}
