package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.params.EstadoBusqueda;
import com.example.demo.services.params.EstadoBusquedaService;

@RestController
@RequestMapping("/estado-busqueda")
public class EstadoBusquedaController extends BaseControllerImpl<EstadoBusqueda, EstadoBusquedaService> {

    private final EstadoBusquedaService estadoBusquedaService;

    public EstadoBusquedaController(EstadoBusquedaService estadoBusquedaService) {
        super(estadoBusquedaService);
        this.estadoBusquedaService = estadoBusquedaService;
    }   
    
}
