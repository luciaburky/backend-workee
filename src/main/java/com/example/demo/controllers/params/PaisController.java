package com.example.demo.controllers.params;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controllers.BaseControllerImpl;
import com.example.demo.entities.params.Pais;
import com.example.demo.entities.params.Provincia;
import com.example.demo.services.params.PaisService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/paises")
@Tag(name = "Pais", description = "Controlador para operaciones CRUD de Pais")
public class PaisController extends BaseControllerImpl<Pais, PaisService> {

    private final PaisService paisService;

    public PaisController(PaisService paisService) {
        super(paisService);
        this.paisService = paisService;
    }


    @Operation(summary = "Obtiene las provincias según el id de País")
    @GetMapping("/{idPais}/provincias") 
    public ResponseEntity<List<Provincia>> getProvinciasByPais(@PathVariable Long idPais) {
       try { 
            List<Provincia> provincias = paisService.getProvinciasByPaisId(idPais);
            return ResponseEntity.ok(provincias);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

