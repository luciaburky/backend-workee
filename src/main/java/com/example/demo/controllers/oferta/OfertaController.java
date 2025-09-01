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

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('GESTION_OFERTAS')")
    public ResponseEntity<Oferta> crearOferta(@Valid @RequestBody OfertaRequestDTO ofertaDTO) {
        Oferta nuevaOferta = ofertaService.crearOferta(ofertaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaOferta);
    }
   
    @Operation(summary = "Obtener una Oferta por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GESTION_OFERTAS')")
    public ResponseEntity<Oferta> getOfertaById(@PathVariable("id") Long id) {
        Oferta oferta = ofertaService.findById(id);
        return ResponseEntity.ok().body(oferta);
    }

    @Operation(summary = "Cambiar el estado de una Oferta")
    @PostMapping("/{id}/cambiar-estado/{nuevoCodigo}")
    @PreAuthorize("hasAuthority('GESTION_OFERTAS')")
    public ResponseEntity<Oferta> cambiarEstado(@PathVariable("id") Long id, @PathVariable("nuevoCodigo") String nuevoCodigo) {
        Oferta ofertaActualizada = ofertaService.cambiarEstado(id, nuevoCodigo);
        return ResponseEntity.ok(ofertaActualizada);
    }

    @Operation(summary = "Marcar el resultado final de una Oferta")
    @PostMapping("/{id}/marcar-resultado-final")
    @PreAuthorize("hasAuthority('GESTION_OFERTAS')")
    public ResponseEntity<Oferta> marcarResultadoFinal(@PathVariable("id") Long id, @RequestBody boolean conExito) {
        Oferta ofertaActualizada = ofertaService.marcarResultadoFinal(id, conExito);
        return ResponseEntity.ok(ofertaActualizada);
    }

    @Operation(summary = "Obtener todas las Ofertas de una Empresa")
    @GetMapping("/empresa/{empresaId}")
    @PreAuthorize("hasAuthority('GESTION_OFERTAS') or hasAuthority('POSTULAR_OFERTA')")
    public ResponseEntity<List<Oferta>> getOfertasByEmpresaId(@PathVariable("empresaId") Long empresaId) {
        List<Oferta> ofertas = ofertaService.findAllByEmpresaId(empresaId);
        return ResponseEntity.ok().body(ofertas);
    }
    
}
