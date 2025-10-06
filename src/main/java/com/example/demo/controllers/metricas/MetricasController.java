package com.example.demo.controllers.metricas;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.metricas.admin.EstadisticasAdminDTO;
import com.example.demo.dtos.metricas.admin.FiltroFechasDTO;
import com.example.demo.dtos.metricas.candidato.DistribucionPostulacionesPorPaisDTO;
import com.example.demo.dtos.metricas.candidato.EstadisticasCandidatoDTO;
import com.example.demo.dtos.metricas.empresa.DistribucionGenerosDTO;
import com.example.demo.services.metricas.MetricasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/metricas")
@Tag(name = "Metricas", description = "Controlador para las métricas")
public class MetricasController {
    private final MetricasService metricasService;

    public MetricasController(MetricasService metricasService){
        this.metricasService = metricasService;
    }
    //ADMINISTRADOR DEL SISTEMA
    @Operation(summary = "Ver métricas del admin del sistema")
    @PutMapping("/admin")
    @PreAuthorize("hasAuthority('METRICAS_SISTEMA')")
    public ResponseEntity<?> metricasAdminSist(@RequestBody FiltroFechasDTO filtroFechasDTO) {
        EstadisticasAdminDTO estadisticas = metricasService.verEstadisticasAdminSistema(filtroFechasDTO.getFechaDesde(), filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(estadisticas);
    }
    
    //CANDIDATOS
    @Operation(summary = "Ver métricas de un candidato")
    @PutMapping("/candidato/{idCandidato}")
    @PreAuthorize("hasAuthority('METRICAS_CANDIDATO')")
    public ResponseEntity<?> metricasCandidato(@PathVariable Long idCandidato, @RequestBody FiltroFechasDTO filtroFechasDTO) {
        EstadisticasCandidatoDTO estadisticas = metricasService.verEstadisticasCandidato(idCandidato, filtroFechasDTO.getFechaDesde(), filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(estadisticas);
    }

    //EMPRESAS
    @Operation(summary = "EMPRESA: Ver cantidad de ofertas abiertas")
    @GetMapping("/empresa/ofertasAbiertas/{idEmpresa}")
    @PreAuthorize("hasAuthority('METRICAS_EMPRESA')")
    public ResponseEntity<?> verCantidadOfertasAbiertas(@PathVariable Long idEmpresa) {
        Long cantidad = metricasService.obtenerCantidadOfertasAbiertas(idEmpresa);
        return ResponseEntity.ok().body(Map.of("cantidadOfertasAbiertas", cantidad));
    }

    //EMPRESAS
    @Operation(summary = "EMPRESA: Ver proporción de géneros postulados")
    @PutMapping("/empresa/generosEnOfertas/{idEmpresa}")
    @PreAuthorize("hasAuthority('METRICAS_EMPRESA')")
    public ResponseEntity<?> verDistribucionGeneros(@PathVariable Long idEmpresa, @RequestBody FiltroFechasDTO filtroFechasDTO) {
        DistribucionGenerosDTO distribucion = metricasService.distribucionGenerosEnOfertas(idEmpresa, filtroFechasDTO.getFechaDesde(), filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(distribucion);
    }

    @Operation(summary = "EMPRESA: Ver tasa de abandono")
    @PutMapping("/empresa/abandonos/{idEmpresa}")
    @PreAuthorize("hasAuthority('METRICAS_EMPRESA')")
    public ResponseEntity<?> verTasaAbandono(@PathVariable Long idEmpresa, @RequestBody FiltroFechasDTO filtroFechasDTO) {
        Double tasa = metricasService.tasaAbandonoOfertas(idEmpresa, filtroFechasDTO.getFechaDesde(), filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(Map.of("tasaAbandono", tasa));
    }

    @Operation(summary = "EMPRESA: Ver tiempo promedio de contratación (en días)")
    @PutMapping("/empresa/tiempoContratacion/{idEmpresa}")
    @PreAuthorize("hasAuthority('METRICAS_EMPRESA')")
    public ResponseEntity<?> verTiempoPromedioContratacion(@PathVariable Long idEmpresa, @RequestBody FiltroFechasDTO filtroFechasDTO) {
        Double tiempo = metricasService.tiempoPromedioContratacion(idEmpresa, filtroFechasDTO.getFechaDesde(), filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(Map.of("tiempoPromedioContratacion", tiempo));
    }

    @Operation(summary = "EMPRESA: Ver localización de candidatos postulados")
    @PutMapping("/empresa/localizacionCandidatos/{idEmpresa}")
    @PreAuthorize("hasAuthority('METRICAS_EMPRESA')") //NOTA: recicle el DTO, pero no se le pasa el % porque este solo pedia la cant
    public ResponseEntity<?> localizacionCandidatos(@PathVariable Long idEmpresa, @RequestBody FiltroFechasDTO filtroFechasDTO) {
        DistribucionPostulacionesPorPaisDTO distribucion = metricasService.localizacionCandidatos(idEmpresa, filtroFechasDTO.getFechaDesde(), filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(distribucion);
    }
}