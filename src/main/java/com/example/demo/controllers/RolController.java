package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.seguridad.Rol;
import com.example.demo.services.seguridad.RolService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path ="/roles")
@Tag(name = "Rol", description = "Controlador para operaciones de Rol")
public class RolController {
    private final RolService rolService;

    public RolController(RolService rolService){
        this.rolService = rolService;
    }

    @Operation(summary = "Obtiene todos los roles")
    @GetMapping("")
    public ResponseEntity<?> obtenerRoles(){
        List<Rol> roles = rolService.obtenerRoles();
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }
}