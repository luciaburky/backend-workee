package com.example.demo.controllers.params;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controllers.BaseControllerImpl;
import com.example.demo.entities.params.Habilidad;
import com.example.demo.services.params.HabilidadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/habilidades")
@Tag(name = "Habilidad", description = "Controlador para operaciones CRUD de Habilidad")
public class HabilidadController {
 
    private final HabilidadService habilidadService;
    
    public HabilidadController(HabilidadService habilidadService) {
        this.habilidadService = habilidadService;
    }

    @Operation(summary = "Obtiene todas las habilidades segun el id de Tipo de Habilidad")
    @GetMapping("habilidadesPorTipoHabilidad/{idTipoHabilidad}")
    public ResponseEntity<?> getHabilidadesByTipoHabilidad(@PathVariable Long idTipoHabilidad) {
       /*  try {
            return ResponseEntity.ok(habilidadService.findByTipoHabilidad(idTipoHabilidad));
        } catch (Exception e) {
            return errorResponse("No se pudo obtener la habilidad", HttpStatus.BAD_REQUEST);
        } */
    } 
}

