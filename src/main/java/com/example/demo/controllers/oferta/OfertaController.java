package com.example.demo.controllers.oferta;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.OfertaRequestDTO;
import com.example.demo.entities.oferta.Oferta;
import com.example.demo.services.oferta.OfertaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/ofertas")
@Tag(name = "Oferta", description = "Controlador para operaciones CRUD")
public class OfertaController {

    private final OfertaService ofertaService;

    public OfertaController(OfertaService ofertaService) {
        this.ofertaService = ofertaService;
    }
    
    @Operation(summary = "Crear una nueva Oferta")
    @PostMapping
    public ResponseEntity<Oferta> crearOferta(@Valid @RequestBody OfertaRequestDTO ofertaDTO) {
        Oferta nuevaOferta = ofertaService.crearOferta(ofertaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaOferta);
    }
   
    @Operation(summary = "Obtener una Oferta por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Oferta> getOfertaById(@PathVariable("id") Long id) {
        Oferta oferta = ofertaService.findById(id);
        return ResponseEntity.ok().body(oferta);
    }

    @Operation(summary = "Cambiar el estado de una Oferta")
    @PostMapping("/{id}/cambiar-estado/{nuevoCodigo}")
    public ResponseEntity<Oferta> cambiarEstado(@PathVariable("id") Long id, @PathVariable("nuevoCodigo") String nuevoCodigo) {
        Oferta ofertaActualizada = ofertaService.cambiarEstado(id, nuevoCodigo);
        return ResponseEntity.ok(ofertaActualizada);
    }

    @Operation(summary = "Marcar el resultado final de una Oferta")
    @PostMapping("/{id}/marcar-resultado-final")
    public ResponseEntity<Oferta> marcarResultadoFinal(@PathVariable("id") Long id, @RequestBody boolean conExito) {
        Oferta ofertaActualizada = ofertaService.marcarResultadoFinal(id, conExito);
        return ResponseEntity.ok(ofertaActualizada);
    }
    
}
