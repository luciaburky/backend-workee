package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.params.Habilidad;
import com.example.demo.services.HabilidadService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/habilidades")
public class HabilidadController extends BaseControllerImpl<Habilidad, HabilidadService> {
 
    private final HabilidadService habilidadService;
    
    public HabilidadController(HabilidadService habilidadService) {
        super(habilidadService);
        this.habilidadService = habilidadService;
    }

    @GetMapping("/tipoHabilidad/{id}")
    public ResponseEntity<?> getByTipoHabilidad(@PathVariable Long idTipoHabilidad) {
        try {
            return ResponseEntity.ok(habilidadService.findByTipoHabilidad(idTipoHabilidad));
        } catch (Exception e) {
            return errorResponse("No se pudo obtener la habilidad", HttpStatus.BAD_REQUEST);
        }
    }
}

