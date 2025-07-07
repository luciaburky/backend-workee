package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.params.TipoEvento;
import com.example.demo.services.params.TipoEventoService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/tipos-evento")
@Tag(name = "TipoEvento", description = "Controlador para operaciones CRUD de TipoEvento")
public class TipoEventoController extends BaseControllerImpl<TipoEvento, TipoEventoService> {

    private final TipoEventoService tipoEventoService;

    public TipoEventoController(TipoEventoService tipoEventoService) {
        super(tipoEventoService);
        this.tipoEventoService = tipoEventoService;
    }

}
