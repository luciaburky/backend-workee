package com.example.demo.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Base;
import com.example.demo.services.BaseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Base", description = "Controller base para operaciones CRUD genéricas")
public abstract class BaseControllerImpl<E extends Base, S extends BaseService<E, Long>> implements BaseController<E, Long> {
    
    private final S servicio;

    public BaseControllerImpl(S servicio) {
        this.servicio = servicio;   
    }

    @Operation(summary = "Obtener todas las entidades", description = "Recupera una lista de todas las entidades")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Lista de entidades recuperada exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(servicio.findAll());
        } catch (Exception e) {
            return errorResponse("No se pudieron obtener los datos", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Obtener una entidad por ID", description = "Recupera una única entidad por su ID")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Entidad recuperada exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(servicio.findById(id));
        } catch (Exception e) {
            return errorResponse("El recurso no fue encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Crear una nueva entidad", description = "Crea y guarda una nueva entidad en la base de datos")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "201", description = "Entidad creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody E entity) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(servicio.save(entity));
        } catch (Exception e) {
            return errorResponse("No se pudo guardar el recurso", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Modificar una entidad", description = "Actualiza una entidad existente utilizando su ID")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Entidad modificada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody E entity) {
        try {
            return ResponseEntity.ok(servicio.update(id, entity));
        } catch (Exception e) {
            return errorResponse("No se pudo actualizar el recurso", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Eliminar una entidad", description = "Elimina una entidad específica utilizando su ID")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200", description = "Entidad eliminada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            servicio.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return errorResponse("No se pudo eliminar el recurso", HttpStatus.BAD_REQUEST);
        }
    }

    // Utilidad para construir respuestas de error JSON uniformes.
    protected ResponseEntity<Map<String, String>> errorResponse(String message, HttpStatus status) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        error.put("status", String.valueOf(status.value()));
        error.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.status(status).body(error);
    }
}

