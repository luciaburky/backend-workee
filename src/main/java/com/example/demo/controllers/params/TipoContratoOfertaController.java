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

import com.example.demo.dtos.params.TipoContratoOfertaRequestDTO;
import com.example.demo.entities.params.TipoContratoOferta;
import com.example.demo.services.params.TipoContratoOfertaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tipos-contrato-oferta")
@Tag(name = "TipoContratoOferta", description = "Controlador para operaciones CRUD de TipoContratoOferta")
public class TipoContratoOfertaController {

    private final TipoContratoOfertaService tipoContratoOfertaService;

    public TipoContratoOfertaController(TipoContratoOfertaService tipoContratoOfertaService) {
        this.tipoContratoOfertaService = tipoContratoOfertaService;
    }

    @Operation(summary = "Crear un nuevo TipoContratoOferta")
    @PostMapping("")
    public ResponseEntity<?> crearTipoContratoOferta(@Valid @RequestBody TipoContratoOfertaRequestDTO tipoContratoOfertaRequestDTO){
        TipoContratoOferta nuevoTipoContratoOferta = tipoContratoOfertaService.guardarTipoContratoOferta(tipoContratoOfertaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTipoContratoOferta);
    }

    @Operation(summary = "Actualizar un TipoContratoOferta existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarTipoContratoOferta(@PathVariable Long id, @RequestBody TipoContratoOfertaRequestDTO tipoContratoOfertaRequestDTO) {
        TipoContratoOferta tipoContratoOfertaActualizado = tipoContratoOfertaService.actualizarTipoContratoOferta(id, tipoContratoOfertaRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(tipoContratoOfertaActualizado);
    }

    @Operation(summary = "Obtiene todos los TipoContratoOferta")
    @GetMapping("")
    public ResponseEntity<?> obtenerTiposContratosOferta(){
        List<TipoContratoOferta> tiposContratosOferta = tipoContratoOfertaService.obtenerTiposContratosOferta();
        return ResponseEntity.status(HttpStatus.OK).body(tiposContratosOferta);
    }

    @Operation(summary = "Obtiene todos los TipoContratoOferta ACTIVOS")
    @GetMapping("/activos")
    public ResponseEntity<?> obtenerTiposContratosOfertaActivos(){
        List<TipoContratoOferta> tiposContratosOfertaActivos = tipoContratoOfertaService.obtenerTiposContratosOfertaActivos();
        return ResponseEntity.status(HttpStatus.OK).body(tiposContratosOfertaActivos);
    }
    
    @Operation(summary = "Obtiene un TipoContratoOferta por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerTipoContratoOfertaPorId(@PathVariable Long id) {
        TipoContratoOferta tipoContratoOferta = tipoContratoOfertaService.findById(id); //buscarTipoContratoOfertaPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(tipoContratoOferta);
    }

    @Operation(summary = "Deshabilita un TipoContratoOferta")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deshabilitarTipoContratoOferta(@PathVariable Long id) {
        tipoContratoOfertaService.delete(id); //deshabilitarTipoContratoOferta(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Habilita un TipoContratoOferta")
    @PutMapping("/habilitar/{id}")
    public ResponseEntity<?> habilitarTipoContratoOferta(@PathVariable Long id) {
        tipoContratoOfertaService.habilitarTipoContratoOferta(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
