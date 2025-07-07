package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.params.EstadoUsuario;
import com.example.demo.services.params.EstadoUsuarioService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/estados-usuario")
@Tag(name = "EstadoUsuario", description = "Controlador para operaciones CRUD de EstadoUsuario")
public class EstadoUsuarioController extends BaseControllerImpl<EstadoUsuario, EstadoUsuarioService> {
    
    private final EstadoUsuarioService estadoUsuarioService;

    public EstadoUsuarioController(EstadoUsuarioService estadoUsuarioService) {
        super(estadoUsuarioService);
        this.estadoUsuarioService = estadoUsuarioService;
    }
}
