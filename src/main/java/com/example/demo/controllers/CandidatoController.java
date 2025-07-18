package com.example.demo.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.CandidatoRequestDTO;
import com.example.demo.entities.Candidato;
import com.example.demo.services.candidato.CandidatoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(path = "/candidatos")
@Tag(name = "candidatos", description = "Controlador para operaciones de Candidato")
public class CandidatoController {
    
    private final CandidatoService candidatoService;

    public CandidatoController(CandidatoService candidatoService){
        this.candidatoService = candidatoService;
    }

    @Operation(summary = "Crear un nuevo Candidato")
    @PostMapping
        public ResponseEntity<Candidato> crearCandidato(@Valid @RequestBody CandidatoRequestDTO candidatoDTO) {
        Candidato nuevoCandidato = candidatoService.crearCandidato(candidatoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCandidato);
    }

    @Operation(summary = "Actualizar un Candidato")
    @PutMapping("/{id}")
    public ResponseEntity<Candidato> actualizarCandidato(@RequestParam Long idCandidato, CandidatoRequestDTO candidatoDTO) {
        Candidato candidatoActualizado = candidatoService.modificarCandidato(idCandidato, candidatoDTO);
        return ResponseEntity.ok().body(candidatoActualizado);
    }

    @Operation(summary = "Obtener un Candidato por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<Candidato> obtenerCandidatoPorId(@RequestParam Long idCandidato) {
        Candidato candidato = candidatoService.findById(idCandidato);
        return ResponseEntity.ok().body(candidato);
    }

    @Operation(summary = "Obtener todos los Candidatos")
    @GetMapping
    public ResponseEntity<List<Candidato>> obtenerCandidatos() {
        List<Candidato> listaCandidatos = candidatoService.obtenerCandidatos();
        return ResponseEntity.ok().body(listaCandidatos);
    }
    
}
