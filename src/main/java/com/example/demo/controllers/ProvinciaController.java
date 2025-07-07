package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.params.Provincia;
import com.example.demo.services.params.ProvinciaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/provincias")
@Tag(name = "Provincias")
public class ProvinciaController {

    private final ProvinciaService provinciaService;
    
    public ProvinciaController(ProvinciaService provinciaService) {
        this.provinciaService = provinciaService;
    }

    @Operation(summary = "Obtiene las provincias según el id de País")
    @GetMapping("/por-pais/{idPais}") 
    public ResponseEntity<List<Provincia>> getProvinciasByPais(@PathVariable Long idPais) {
       try { 
            List<Provincia> provincias = provinciaService.findProvinciaByPaisId(idPais);
            return ResponseEntity.ok(provincias);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
