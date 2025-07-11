package com.example.demo.controllers.params;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controllers.BaseControllerImpl;
import com.example.demo.entities.params.Genero;
import com.example.demo.services.params.GeneroService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/generos")
@Tag(name = "Genero", description = "Controlador para operaciones CRUD de Genero")
public class GeneroController {

    private final GeneroService generoService;

    public GeneroController(GeneroService generoService) {
        this.generoService = generoService;
    }

}

