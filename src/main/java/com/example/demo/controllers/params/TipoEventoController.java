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

import com.example.demo.dtos.params.TipoEventoRequestDTO;
import com.example.demo.entities.params.TipoEvento;
import com.example.demo.services.params.TipoEventoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tipos-evento")
@Tag(name = "TipoEvento", description = "Controlador para operaciones CRUD de TipoEvento")
public class TipoEventoController  {

    private final TipoEventoService tipoEventoService;

    public TipoEventoController(TipoEventoService tipoEventoService) {
        this.tipoEventoService = tipoEventoService;
    }

    @Operation(summary = "Crear un nuevo TipoEvento")
    @PostMapping("")
    public ResponseEntity<?> TipoEvento(@Valid @RequestBody TipoEventoRequestDTO tipoEventoRequestDTO){
        TipoEvento nuevoTipoEvento = tipoEventoService.guardarTipoEvento(tipoEventoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTipoEvento);
    }

    @Operation(summary = "Actualizar un TipoEvento existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> TipoEvento(@PathVariable Long id, @RequestBody TipoEventoRequestDTO tipoEventoRequestDTO) {
        TipoEvento tipoEventoActualizado = tipoEventoService.actualizarTipoEvento(id, tipoEventoRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(tipoEventoActualizado);
    }

    @Operation(summary = "Obtiene todos los TipoEvento")
    @GetMapping("")
    public ResponseEntity<?> TipoEvento(){
        List<TipoEvento> tiposEventos = tipoEventoService.obtenerTiposEventos();
        return ResponseEntity.status(HttpStatus.OK).body(tiposEventos);
    }

    @Operation(summary = "Obtiene todos los TipoEvento ACTIVOS")
    @GetMapping("/activos")
    public ResponseEntity<?> obtenerEstadosUsuarioActivos(){
        List<TipoEvento> tiposEventosActivos = tipoEventoService.obtenerTiposEventosActivos();
        return ResponseEntity.status(HttpStatus.OK).body(tiposEventosActivos);
    }
    
    @Operation(summary = "Obtiene un TipoEvento por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> TipoEventoPorId(@PathVariable Long id) {
        TipoEvento tipoEvento = tipoEventoService.buscarTipoEventoPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(tipoEvento);
    }

    @Operation(summary = "Deshabilita un TipoEvento")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deshabilitarTipoEvento(@PathVariable Long id) {
        tipoEventoService.deshabilitarTipoEvento(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Habilita un TipoEvento")
    @PutMapping("/habilitar/{id}")
    public ResponseEntity<?> habilitaripoEvento(@PathVariable Long id) {
        tipoEventoService.habilitarTipoEvento(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
