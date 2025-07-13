package com.example.demo.controllers.params;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.params.EstadoUsuarioRequestDTO;
import com.example.demo.entities.params.EstadoUsuario;
import com.example.demo.services.params.EstadoUsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/estados-usuario")
@Tag(name = "EstadoUsuario", description = "Controlador para operaciones CRUD de EstadoUsuario")
public class EstadoUsuarioController {
    
    private final EstadoUsuarioService estadoUsuarioService;

    public EstadoUsuarioController(EstadoUsuarioService estadoUsuarioService) {
        this.estadoUsuarioService = estadoUsuarioService;
    }

    @Operation(summary = "Crear un nuevo EstadoUsuario")
    @PostMapping("")
    public ResponseEntity<?> crearEstadoUsuario(@Valid @RequestBody EstadoUsuarioRequestDTO estadoUsuarioRequestDTO){
        EstadoUsuario nuevoEstadoUsuario = estadoUsuarioService.guardarEstadoUsuario(estadoUsuarioRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEstadoUsuario);
    }

    @Operation(summary = "Actualizar un EstadoUsuario existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEstadoUsuario(@PathVariable Long id, @RequestBody EstadoUsuarioRequestDTO estadoUsuarioRequestDTO) {
        EstadoUsuario estadoUsuarioActualizado = estadoUsuarioService.actualizarEstadoUsuario(id, estadoUsuarioRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(estadoUsuarioActualizado);
    }

    @Operation(summary = "Obtiene todos los EstadoUsuario")
    @GetMapping("")
    public ResponseEntity<?> obtenerEstadosUsuario(){
        List<EstadoUsuario> estadosUsuario = estadoUsuarioService.obtenerEstadosUsuario();
        return ResponseEntity.status(HttpStatus.OK).body(estadosUsuario);
    }

    @Operation(summary = "Obtiene todos los EstadoUsuario ACTIVOS")
    @GetMapping("/activos")
    public ResponseEntity<?> obtenerEstadosUsuarioActivos(){
        List<EstadoUsuario> estadosUsuarioActivos = estadoUsuarioService.obtenerEstadosUsuarioActivos();
        return ResponseEntity.status(HttpStatus.OK).body(estadosUsuarioActivos);
    }
    
    @Operation(summary = "Obtiene un EstadoUsuario por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerEstadoUsuarioPorId(@PathVariable Long id) {
        EstadoUsuario estadoUsuario = estadoUsuarioService.findById(id); //buscarEstadoUsuarioPorId(id);
        if (estadoUsuario != null) {
            return ResponseEntity.status(HttpStatus.OK).body(estadoUsuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("EstadoUsuario no encontrado");
        }
    }

    @Operation(summary = "Deshabilita un EstadoUsuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deshabilitarEstadoUsuario(@PathVariable Long id) {
        boolean eliminado = estadoUsuarioService.delete(id);//deshabilitarEstadoUsuario(id);
        if (eliminado) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("EstadoUsuario no encontrado");
        }
    }

    @Operation(summary = "Habilita un EstadoUsuario")
    @PutMapping("/habilitar/{id}")
    public ResponseEntity<?> habilitarEstadoUsuario(@PathVariable Long id) {
        Boolean habilitado = estadoUsuarioService.habilitarEstadoUsuario(id);
        if (habilitado) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("EstadoUsuario no encontrado");
        }
    }

}
