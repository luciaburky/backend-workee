package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.EmpleadoEmpresaRequestDTO;
import com.example.demo.entities.empresa.EmpleadoEmpresa;
import com.example.demo.services.empresa.EmpleadoEmpresaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
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

    @Operation(summary = "Un EmpleadoEmpresa actualiza su perfil")
    @PutMapping("/actualizarPerfilPropio/{id}")
    public ResponseEntity<?> actualizarEmpleadoComoEmpleado(@RequestBody EmpleadoEmpresaRequestDTO empleadoEmpresaRequestDTO, Long id){
        EmpleadoEmpresa empleadoEmpresa = empleadoEmpresaService.modificarEmpleado(empleadoEmpresaRequestDTO,true, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoEmpresa);
    }

    @Operation(summary = "Un Administrador de Empresa actualiza el perfil de un Empleado")
    @PutMapping("/actualizarPerfilPorAdmin/{id}")
    public ResponseEntity<?> actualizarEmpleadoComoAdminEmpresa(@RequestBody EmpleadoEmpresaRequestDTO empleadoEmpresaRequestDTO, Long id){
        EmpleadoEmpresa empleadoEmpresa = empleadoEmpresaService.modificarEmpleado(empleadoEmpresaRequestDTO,false, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoEmpresa);
    }


    @Operation(summary = "Trae todos los empleados ACTIVOS de una empresa")
    @PutMapping("/traerTodos/{id}")
    public ResponseEntity<?> visualizarEmpleadosActivos(@PathVariable Long id){
        List<EmpleadoEmpresa> empleados = empleadoEmpresaService.visualizarEmpleados(id);
        return ResponseEntity.status(HttpStatus.OK).body(empleados);
    }

    @Operation(summary = "Dice la cantidad de empleados ACTIVOS que tiene una empresa")
    @PutMapping("/contarEmpleados/{id}")
    public ResponseEntity<?> contarCantidadEmpleadosDeEmpresa(@PathVariable Long id){
        Long cantidadEmpleados = empleadoEmpresaService.contarEmpleadosDeEmpresa(id);
        return ResponseEntity.status(HttpStatus.OK).body(cantidadEmpleados);
    }


    @Operation(summary = "Trae a un EmpleadoEmpresa por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> visualizarDatosEmpleado(@PathVariable Long id){
        EmpleadoEmpresa empleado = empleadoEmpresaService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(empleado);
    }

    //TODO: Agregar un endpoint (creo que en el controlador de oferta) para que traiga todas las ofertas asociadas a un empleado
}


