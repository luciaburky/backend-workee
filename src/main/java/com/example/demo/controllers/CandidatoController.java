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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Operation(summary = "Actualizar un Candidato")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ACTUALIZAR_MI_PERFIL')")
    public ResponseEntity<Candidato> actualizarCandidato(@PathVariable("id") Long idCandidato, @RequestBody CandidatoRequestDTO candidatoDTO) {
        Candidato candidatoActualizado = candidatoService.modificarCandidato(idCandidato, candidatoDTO);
        return ResponseEntity.ok().body(candidatoActualizado);
    }

    @Operation(summary = "Obtener un Candidato por su ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BUSCAR_CANDIDATOS') or hasAuthority('VER_PERFIL_CANDIDATO')")
    public ResponseEntity<Candidato> obtenerCandidatoPorId(@RequestParam Long idCandidato) {
        Candidato candidato = candidatoService.findById(idCandidato);
        return ResponseEntity.ok().body(candidato);
    }

    /*@Operation(summary = "Obtener todos los Candidatos")
    @GetMapping("")
    public ResponseEntity<List<Candidato>> obtenerCandidatos() {
        List<Candidato> listaCandidatos = candidatoService.obtenerCandidatos();
        return ResponseEntity.ok().body(listaCandidatos);
    }*/

    @Operation(summary = "Obtener habilidades de un Candidato")
    @GetMapping("/{id}/habilidades")
    @PreAuthorize("hasAuthority('BUSCAR_CANDIDATOS') or hasAuthority('VER_PERFIL_CANDIDATO') or hasAuthority('ACTUALIZAR_CANDIDATO')") //TODO: agregarle los que falten
    public ResponseEntity<List<Habilidad>> obtenerHabilidades(@RequestParam Long idCandidato) {
        List<Habilidad> habilidades = candidatoService.obtenerHabilidades(idCandidato);
        return ResponseEntity.ok().body(habilidades);
    }

    @Operation(summary = "Elimina una cuenta de candidato y todo lo relacionado a ella")
    @DeleteMapping("/{idCandidato}")
    @PreAuthorize("hasAuthority('ELIMINAR_CANDIDATO')")
    public ResponseEntity<?> eliminarCandidato(@PathVariable Long idCandidato){
        candidatoService.eliminarCuentaCandidato(idCandidato);
        return ResponseEntity.status(HttpStatus.OK).body("Cuenta de candidato eliminada correctamente");
    }

}
