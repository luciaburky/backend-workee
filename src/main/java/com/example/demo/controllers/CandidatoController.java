package com.example.demo.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.CandidatoRequestDTO;
import com.example.demo.entities.candidato.Candidato;
import com.example.demo.entities.params.Habilidad;
import com.example.demo.services.candidato.CandidatoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(path = "/candidatos")
@Tag(name = "Candidato", description = "Controlador para operaciones de Candidato")
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
    public ResponseEntity<Candidato> actualizarCandidato(@PathVariable("id") Long idCandidato, @RequestBody CandidatoRequestDTO candidatoDTO) {
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

    @Operation(summary = "Obtener habilidades de un Candidato")
    @GetMapping("/{id}/habilidades")
    public ResponseEntity<List<Habilidad>> obtenerHabilidades(@RequestParam Long idCandidato) {
        List<Habilidad> habilidades = candidatoService.obtenerHabilidades(idCandidato);
        return ResponseEntity.ok().body(habilidades);
    }

    /*Ver si recibir idCandidato o CandidatoRequestDTO y si recibir idHabilidad o Habilidad
    @Operation(summary = "Agregar una habilidad a un Candidato")
    @PostMapping("/{id}/habilidades")   
    public ResponseEntity<List<Habilidad>> agregarHabilidad(@RequestParam Long idCandidato, @RequestParam Long idHabilidad) {
        List<Habilidad> habilidadesActualizadas = candidatoService.agregarHabilidad(idCandidato, idHabilidad);
        return ResponseEntity.ok().body(habilidadesActualizadas);
    }
    
    @Operation(summary = "Eliminar una habilidad de un Candidato")
    @DeleteMapping("/{idCandidato}/habilidades/{idHabilidad}]")  
    public ResponseEntity<List<Habilidad>> eliminarHabilidad(@PathVariable Long idCandidato, @PathVariable Long idHabilidad) {
        List<Habilidad> habilidadesActualizadas = candidatoService.eliminarHabilidad(idCandidato, idHabilidad);
        return ResponseEntity.ok().body(habilidadesActualizadas);
    }
        */

    @Operation(summary = "Elimina una cuenta de candidato y todo lo relacionado a ella")
    @DeleteMapping("/{idCandidato}")
    public ResponseEntity<?> eliminarCandidato(@PathVariable Long idCandidato){
        candidatoService.eliminarCuentaCandidato(idCandidato);
        return ResponseEntity.status(HttpStatus.OK).body("Cuenta de candidato eliminada correctamente");
    }
}
