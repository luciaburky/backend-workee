package com.example.demo.controllers.metricas;

import java.util.List;
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
import com.example.demo.dtos.metricas.candidato.RubrosDeInteresDTO;
import com.example.demo.dtos.metricas.candidato.TopHabilidadDTO;
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
    public ResponseEntity<?> tasaExitoOfertas(@RequestBody FiltroFechasDTO filtroFechasDTO) {
        EstadisticasAdminDTO estadisticas = metricasService.verEstadisticasAdminSistema(filtroFechasDTO.getFechaDesde(), filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(estadisticas);
    }
    
    //CANDIDATOS
    @Operation(summary = "CANDIDATO: Cantidad total de postulaciones en curso")
    @GetMapping("/candidato/enCurso/{idCandidato}")
    @PreAuthorize("hasAuthority('METRICAS_CANDIDATO')")
    public ResponseEntity<?> cantidadPostulacionesEnCurso(@PathVariable Long idCandidato) {
        Long cantidad = metricasService.contarPostulacionesEnCurso(idCandidato);
        return ResponseEntity.ok().body(Map.of("postulacionesEnCurso", cantidad));
    }

    @Operation(summary = "CANDIDATO: Cantidad total de postulaciones rechazadas")
    @PutMapping("/candidato/rechazadas/{idCandidato}")
    @PreAuthorize("hasAuthority('METRICAS_CANDIDATO')")
    public ResponseEntity<?> cantidadPostulacionesRechazadas(@PathVariable Long idCandidato, @RequestBody FiltroFechasDTO filtroFechasDTO) {
        Long cantidad = metricasService.contarPostulacionesRechazadas(idCandidato, filtroFechasDTO.getFechaDesde(), filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(Map.of("postulacionesRechazadas", cantidad));
    }

    @Operation(summary = "CANDIDATO: Ver rubros de interes del candidato")
    @PutMapping("/candidato/rubrosDeInteres/{idCandidato}")
    @PreAuthorize("hasAuthority('METRICAS_CANDIDATO')")
    public ResponseEntity<?> verRubrosDeInteres(@PathVariable Long idCandidato, @RequestBody FiltroFechasDTO filtroFechasDTO) {
        List<RubrosDeInteresDTO> rubros = metricasService.verRubrosDeInteres(idCandidato, filtroFechasDTO.getFechaDesde(), filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(rubros);
    }

    @Operation(summary = "CANDIDATO: Ver paises más postulados del candidato")
    @PutMapping("/candidato/paisesMasPostulados/{idCandidato}")
    @PreAuthorize("hasAuthority('METRICAS_CANDIDATO')")
    public ResponseEntity<?> verPaisesMasPostulados(@PathVariable Long idCandidato, @RequestBody FiltroFechasDTO filtroFechasDTO) {
        DistribucionPostulacionesPorPaisDTO distribucion = metricasService.verPaisesMasPostulados(idCandidato, filtroFechasDTO.getFechaDesde(), filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(distribucion);
    }

    @Operation(summary = "CANDIDATO: Ver top 3 habilidades blandas")
    @PutMapping("/candidato/habilidadesBlandas")
    @PreAuthorize("hasAuthority('METRICAS_CANDIDATO')")
    public ResponseEntity<?> verTopHabilidadesBlandas(@RequestBody FiltroFechasDTO filtroFechasDTO) {
        List<TopHabilidadDTO> habilidades = metricasService.topHabilidadesBlandas(filtroFechasDTO.getFechaDesde(),filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(habilidades);
    }

    @Operation(summary = "CANDIDATO: Ver top 3 habilidades tecnicas")
    @PutMapping("/candidato/habilidadesTecnicas")
    @PreAuthorize("hasAuthority('METRICAS_CANDIDATO')")
    public ResponseEntity<?> verTopHabilidadesTecnicas(@RequestBody FiltroFechasDTO filtroFechasDTO) {
        List<TopHabilidadDTO> habilidades = metricasService.topHabilidadesTecnicas(filtroFechasDTO.getFechaDesde(),filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(habilidades);
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