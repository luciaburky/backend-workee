package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.params.EstadoUsuario;
import com.example.demo.services.BaseServiceImpl;
import com.example.demo.services.params.EstadoUsuarioService;
import com.example.demo.services.params.EstadoUsuarioServiceImpl;

@RestController
@RequestMapping("/estados-usuario")
public class EstadoUsuarioController /*extends BaseControllerImpl<EstadoUsuario, EstadoUsuarioServiceImpl>*/ {
    
    //private final EstadoUsuarioService estadoUsuarioService;

    /*public EstadoUsuarioController(EstadoUsuarioService estadoUsuarioService) {
        super(estadoUsuarioService);
        this.estadoUsuarioService = estadoUsuarioService;
    }*/
}
