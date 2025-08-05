package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.FiltrosEmpresaRequestDTO;
import com.example.demo.dtos.UbicacionDTO;
import com.example.demo.entities.empresa.Empresa;
import com.example.demo.services.empresa.EmpresaService;
import com.example.demo.services.params.ProvinciaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/busquedas")
@Tag(name = "Búsquedas", description = "Controlador para realizar distintos tipos de búsquedas")
public class BusquedaController {

    private final EmpresaService empresaService;
    private final ProvinciaService provinciaService;

    public BusquedaController(EmpresaService empresaService, ProvinciaService provinciaService){
        this.empresaService = empresaService;
        this.provinciaService = provinciaService;
    }

    @Operation(summary = "Trae empresas según los filtros aplicados")
    @PostMapping("/empresasFiltradas")
    @PreAuthorize("hasAuthority('BUSCAR_EMPRESAS')")
    public ResponseEntity<?> filtrarEmpresas(@RequestBody FiltrosEmpresaRequestDTO filtrosEmpresaRequestDTO){
        List<Empresa> empresas = empresaService.buscarEmpresasConFiltros(filtrosEmpresaRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(empresas);
    }

    @Operation(summary = "Trae opciones para el filtro 'Ubicación' (Provincia, Pais)")
    @GetMapping("/filtroUbicacion")
    @PreAuthorize("hasAuthority('BUSCAR_EMPRESAS') or hasAuthority('BUSCAR_OFERTAS') or hasAuthority('BUSCAR_CANDIDATOS')")
    public ResponseEntity<?> visualizarOpcionesFiltroUbicacion(){
        List<UbicacionDTO> ubicaciones = provinciaService.traerUbicaciones();
        System.out.println("ubicaciones " + ubicaciones.getFirst());
        return ResponseEntity.status(HttpStatus.OK).body(ubicaciones);
    }

    @Operation(summary = "Busca empresas según el nombre ingresado")
    @GetMapping("/empresasPorNombre")
    @PreAuthorize("hasAuthority('BUSCAR_EMPRESAS')")
    public ResponseEntity<?> buscarEmpresasPorNombre(@RequestParam String nombreEmpresa){
        List<Empresa> empresas = empresaService.buscarEmpresasPorNombre(nombreEmpresa);
        return ResponseEntity.status(HttpStatus.OK).body(empresas);
    }

}
