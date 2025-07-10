package com.example.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.ModificarEmpresaDTO;
import com.example.demo.entities.Empresa;
import com.example.demo.services.EmpresaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/empresas")
@Tag(name = "Empresa", description = "Controlador para operaciones CRUD de Empresa")
public class EmpresaController extends BaseControllerImpl<Empresa, EmpresaService> {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        super(empresaService);
        this.empresaService = empresaService;
    }

    @Operation(summary = "Actualiza datos del administrador de una empresa")
    @PutMapping("/actualizarEmpresa/{id}")
    public ResponseEntity<Empresa> actualizarEmpresa(@PathVariable Long id, @RequestBody ModificarEmpresaDTO dtoModificarEmpresa) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(empresaService.modificarEmpresa(dtoModificarEmpresa, id));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch(MethodArgumentNotValidException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}


