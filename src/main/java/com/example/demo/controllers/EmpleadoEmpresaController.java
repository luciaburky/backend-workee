package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.EmpleadoEmpresa;
import com.example.demo.services.EmpleadoEmpresaService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path ="/empleados-empresa")
@Tag(name = "EmpleadoEmpresa", description = "Controlador para operaciones CRUD de EmpleadoEmpresa")
public class EmpleadoEmpresaController extends BaseControllerImpl<EmpleadoEmpresa, EmpleadoEmpresaService>{
    private final EmpleadoEmpresaService empleadoEmpresaService;

    public EmpleadoEmpresaController(EmpleadoEmpresaService empleadoEmpresaService) {
        super(empleadoEmpresaService);
        this.empleadoEmpresaService = empleadoEmpresaService;
    }

   

}


