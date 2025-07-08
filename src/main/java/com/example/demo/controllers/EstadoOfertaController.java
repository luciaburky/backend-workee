package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.params.EstadoOferta;
import com.example.demo.services.params.EstadoOfertaService;

@RestController
@RequestMapping("/estado-oferta")
public class EstadoOfertaController extends BaseControllerImpl<EstadoOferta, EstadoOfertaService> {

    private final EstadoOfertaService estadoOfertaService;

    public EstadoOfertaController(EstadoOfertaService estadoOfertaService) {
        super(estadoOfertaService);
        this.estadoOfertaService = estadoOfertaService;
    }
    
}
