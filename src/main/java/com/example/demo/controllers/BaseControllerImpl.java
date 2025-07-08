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

@RestController
public abstract class BaseControllerImpl<E extends Base, S extends BaseService<E, Long> 
    > implements BaseController<E, Long> {
    
    private final S servicio;

    public BaseControllerImpl(S servicio) {
        this.servicio = servicio;   
    }

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(servicio.findAll());
        } catch (Exception e) {
            return errorResponse("No se pudieron obtener los datos", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(servicio.findById(id));
        } catch (Exception e) {
            return errorResponse("El recurso no fue encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody E entity) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(servicio.save(entity));
        } catch (Exception e) {
            return errorResponse("No se pudo guardar el recurso", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody E entity) {
        try {
            return ResponseEntity.ok(servicio.update(id, entity));
        } catch (Exception e) {
            return errorResponse("No se pudo actualizar el recurso", HttpStatus.BAD_REQUEST);
        }
    }

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

