package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.entities.Empresa;
import com.example.demo.services.EmpresaService;


import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/empresas")
@Tag(name = "Empresa", description = "Controlador para operaciones CRUD de Empresa")
public class EmpresaController extends BaseControllerImpl<Empresa, EmpresaService> {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        super(empresaService);
        this.empresaService = empresaService;
    }

    
}


