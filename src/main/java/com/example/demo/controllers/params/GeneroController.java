package com.example.demo.controllers.params;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.params.GeneroRequestDTO;
import com.example.demo.entities.params.Genero;
import com.example.demo.services.params.GeneroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/generos")
@Tag(name = "Genero", description = "Controlador para operaciones CRUD de Genero")
public class GeneroController {

    private final GeneroService generoService;

    public GeneroController(GeneroService generoService) {
        this.generoService = generoService;
    }

    @Operation(summary = "Crear un nuevo Genero")
    @PostMapping("")
    public ResponseEntity<?> crearGenero(@Valid @RequestBody GeneroRequestDTO generoRequestDTO){
        Genero nuevoGenero = generoService.guardarGenero(generoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoGenero);
    }

    @Operation(summary = "Actualizar un Genero existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarGenero(@PathVariable Long id, @RequestBody GeneroRequestDTO generoRequestDTO) {
        Genero generoActualizado = generoService.actualizarGenero(id, generoRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(generoActualizado);
    }

    @Operation(summary = "Obtiene todos los Generos")
    @GetMapping("")
    public ResponseEntity<?> obtenerGeneros(){
        List<Genero> generos = generoService.obtenerGeneros();
        return ResponseEntity.status(HttpStatus.OK).body(generos);
    }

    @Operation(summary = "Obtiene todos los Generos ACTIVOS")
    @GetMapping("/activos")
    public ResponseEntity<?> obtenerGenerosActivos(){
        List<Genero> generosActivos = generoService.obtenerGenerosActivos();
        return ResponseEntity.status(HttpStatus.OK).body(generosActivos);
    }
    
    @Operation(summary = "Obtiene un Genero por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerGeneroPorId(@PathVariable Long id) {
        Genero genero = generoService.findById(id); //buscarGeneroPorId(id);
        if (genero != null) {
            return ResponseEntity.status(HttpStatus.OK).body(genero);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Genero no encontrado");
        }
    }

    @Operation(summary = "Deshabilita un Genero")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deshabilitarGenero(@PathVariable Long id) {
        boolean eliminado = generoService.delete(id); //deshabilitarGenero(id);
        if (eliminado) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Genero no encontrado");
        }
    }

    @Operation(summary = "Habilita un Genero")
    @PutMapping("/habilitar/{id}")
    public ResponseEntity<?> habilitarGenero(@PathVariable Long id) {
        Boolean habilitado = generoService.habilitarGenero(id);
        if (habilitado) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Genero no encontrado");
        }
    }

}

