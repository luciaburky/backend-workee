package com.example.demo.controllers.params;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controllers.BaseControllerImpl;
import com.example.demo.entities.params.Pais;
import com.example.demo.services.params.PaisService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/paises")
@Tag(name = "Pais", description = "Controlador para operaciones CRUD de Pais")
public class PaisController extends BaseControllerImpl<Pais, PaisService> {

    private final PaisService paisService;

    public PaisController(PaisService paisService) {
        super(paisService);
        this.paisService = paisService;
    }


    
}

