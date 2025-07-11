package com.example.demo.controllers.params;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controllers.BaseControllerImpl;
import com.example.demo.dtos.params.PaisRequestDTO;
import com.example.demo.entities.params.Pais;
import com.example.demo.services.params.PaisService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/paises")
@Tag(name = "Pais", description = "Controlador para operaciones CRUD de Pais")
public class PaisController  {

    private final PaisService paisService;

    public PaisController(PaisService paisService) {
        this.paisService = paisService;
    }


    @Operation(summary = "Crea un pais")
    @PostMapping
    public ResponseEntity<?> crearPais(@Valid @RequestBody PaisRequestDTO paisRequestDTO) {
        
        return ResponseEntity.status(HttpStatus.CREATED).body(paisService.guardarPais(paisRequestDTO));
    }

    /*@Operation(summary = "Actualiza un pais")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPais(@PathVariable Long id, @RequestBody PaisRequestDTO paisRequestDTO) {
        return 
    }

    @Operation(summary = "Obtiene todos los paises")
    @GetMapping("")
    public ResponseEntity<?> obtenerPaises() {
        
    }

    @Operation(summary = "Obtiene un pais por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPais() {
        
    }
    @Operation(summary = "Obtiene un pais por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPais(){

    }*/

    
}

