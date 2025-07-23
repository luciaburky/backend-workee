package com.example.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.EmpresaRequestDTO;
import com.example.demo.entities.empresa.Empresa;
import com.example.demo.services.empresa.EmpresaOrquestadorService;
import com.example.demo.services.empresa.EmpresaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/empresas")
@Tag(name = "Empresa", description = "Controlador para operaciones CRUD de Empresa")
public class EmpresaController {

    private final EmpresaService empresaService;
    private final EmpresaOrquestadorService empresaOrquestadorService;

    public EmpresaController(EmpresaService empresaService, EmpresaOrquestadorService empresaOrquestadorService) {
        this.empresaService = empresaService;
        this.empresaOrquestadorService = empresaOrquestadorService;
    }

    @Operation(summary = "Modificar perfil de administrador empresa")
    @PutMapping("/modificarPerfil/{id}")
    public ResponseEntity<?> modificarEmpresa(@PathVariable Long id, @RequestBody EmpresaRequestDTO empresaRequestDTO){
        Empresa empresaModificada = empresaService.modificarEmpresa(id, empresaRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(empresaModificada);
    }

    @Operation(summary = "Ver detalle de una empresa segun su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> visualizarDetalleEmpresa(@PathVariable Long id){
        Empresa empresa = empresaService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(empresa);
    }

    @Operation(summary = "Elimina una cuenta de empresa y todo lo relacionado a ella")
    @DeleteMapping("/{idEmpresa}")
    public ResponseEntity<?> eliminarEmpresa(@PathVariable Long idEmpresa){
        empresaOrquestadorService.darDeBajaEmpresaYRelacionados(idEmpresa);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}


