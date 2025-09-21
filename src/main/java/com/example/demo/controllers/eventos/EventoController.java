package com.example.demo.controllers.eventos;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/eventos")
@Tag(name = "Evento", description = "Controlador para operaciones CRUD de Evento")

public class EventoController {

    
}
