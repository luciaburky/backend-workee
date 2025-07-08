package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.params.Genero;
import com.example.demo.services.params.GeneroService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/generos")
@Tag(name = "Genero", description = "Controlador para operaciones CRUD de Genero")
public class GeneroController extends BaseControllerImpl<Genero, GeneroService> {

    private final GeneroService generoService;

    public GeneroController(GeneroService generoService) {
        super(generoService);
        this.generoService = generoService;
    }

}

