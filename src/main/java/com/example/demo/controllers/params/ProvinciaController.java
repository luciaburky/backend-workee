package com.example.demo.controllers.params;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.params.ProvinciaRequestDTO;
import com.example.demo.entities.params.Provincia;
import com.example.demo.services.params.ProvinciaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/provincias")
@Tag(name = "Provincia", description = "Controlador para operaciones CRUD de Provincias")
public class ProvinciaController {

    private final ProvinciaService provinciaService;
    
    public ProvinciaController(ProvinciaService provinciaService) {
        this.provinciaService = provinciaService;
    }

    @Operation(summary = "Obtiene las provincias según el id de País")
    @GetMapping("provinciasPorPais/{idPais}") 
    public ResponseEntity<List<Provincia>> getProvinciasByPais(@PathVariable Long idPais) {
       try { 
            List<Provincia> provincias = provinciaService.findProvinciaByPaisId(idPais);
            return ResponseEntity.ok(provincias);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Crea una provincia")
    @PostMapping("")
    public ResponseEntity<?> crearProvincia(@Valid @RequestBody ProvinciaRequestDTO provinciaRequestDTO) {
        Provincia nuevaProvincia = provinciaService.guardarProvincia(provinciaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaProvincia);
    }

    @Operation(summary = "Actualiza una provincia")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProvincia(@PathVariable Long id, @RequestBody ProvinciaRequestDTO provinciaRequestDTO) {
        Provincia provinciaActualizada = provinciaService.actualizarProvincia(id, provinciaRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(provinciaActualizada);
    }

    @Operation(summary = "Obtiene todas las provincias")
    @GetMapping("")
    public ResponseEntity<?> obtenerProvincias() {
        List<Provincia> provincias = provinciaService.obtenerProvincias();
        return ResponseEntity.status(HttpStatus.OK).body(provincias);
    }

    @Operation(summary = "Obtiene todas las provincias ACTIVAS")
    @GetMapping("/activas")
    public ResponseEntity<?> obtenerProvinciasActivas() {
        List<Provincia> provincias = provinciaService.obtenerProvinciasActivas();
        return ResponseEntity.status(HttpStatus.OK).body(provincias);
    }

    @Operation(summary = "Obtiene una provincia por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProvincia(@PathVariable Long id) {
        Provincia provincia = provinciaService.findById(id); //buscarProvinciaPorId(id);
        if (provincia != null) {
            return ResponseEntity.status(HttpStatus.OK).body(provincia);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Provincia no encontrada");
        }
    }


    @Operation(summary = "Deshabilita una provincia")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deshabilitarProvincia(@PathVariable Long id) {
        boolean eliminada = provinciaService.deshabilitarProvincia(id); //delete(id);
        if (eliminada) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Provincia no encontrada");
        }

    }

    @Operation(summary = "Habilita una provincia")
    @PutMapping("/habilitar/{id}")
    public ResponseEntity<?> habilitarProvincia(@PathVariable Long id) {
        boolean habilitada = provinciaService.habilitarProvincia(id);
        if (habilitada) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Provincia no encontrada");
        }
    }

}
