package com.example.demo.controllers;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.EmpleadoEmpresaRequestDTO;
import com.example.demo.entities.EmpleadoEmpresa;
import com.example.demo.services.EmpleadoEmpresaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path ="/empleados-empresa")
@Tag(name = "EmpleadoEmpresa", description = "Controlador para operaciones de EmpleadoEmpresa")
public class EmpleadoEmpresaController {
    private final EmpleadoEmpresaService empleadoEmpresaService;

    public EmpleadoEmpresaController(EmpleadoEmpresaService empleadoEmpresaService) {
        this.empleadoEmpresaService = empleadoEmpresaService;
    }

    @Operation(summary = "Crear un empleado empresa")
    @PostMapping("")
    public ResponseEntity<?> crearEmpleado(@Valid @RequestBody EmpleadoEmpresaRequestDTO empleadoEmpresaRequestDTO){
        EmpleadoEmpresa nuevoEmpleadoEmpresa = empleadoEmpresaService.darDeAltaEmpleado(empleadoEmpresaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEmpleadoEmpresa);
    }

}


