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

import com.example.demo.dtos.metricas.admin.DistribucionUsuariosPorRolResponseDTO;
import com.example.demo.dtos.metricas.admin.EmpresasConMasOfertasDTO;
import com.example.demo.dtos.metricas.admin.EvolucionUsuariosDTO;
import com.example.demo.dtos.metricas.admin.FiltroFechasDTO;
import com.example.demo.dtos.metricas.admin.UsuariosPorPaisDTO;
import com.example.demo.dtos.metricas.candidato.DistribucionPostulacionesPorPaisDTO;
import com.example.demo.dtos.metricas.candidato.RubrosDeInteresDTO;
import com.example.demo.dtos.metricas.candidato.TopHabilidadDTO;
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
    @Operation(summary = "Ver cantidad histórica de usuarios")
    @GetMapping("/admin/cantidadHistoricaUsuarios")
    @PreAuthorize("hasAuthority('METRICAS_SISTEMA')")
    public ResponseEntity<?> cantidadHistoricaUsuarios() {
        Integer cantidad = metricasService.cantidadTotalHistoricaUsuarios();
        return ResponseEntity.ok().body(Map.of("cantidadHistoricaUsuarios", cantidad));
    }

    @Operation(summary = "Ver tasa de éxito de ofertas")
    @PutMapping("/admin/tasaExitoOfertas")
    @PreAuthorize("hasAuthority('METRICAS_SISTEMA')")
    public ResponseEntity<?> tasaExitoOfertas(@RequestBody FiltroFechasDTO filtroFechasDTO) {
        Double tasa = metricasService.tasaExitoOfertas(filtroFechasDTO.getFechaDesde(), filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(Map.of("tasaExitoOfertas", tasa));
    }

    @Operation(summary = "Ver distribución de usuarios por rol")
    @PutMapping("/admin/distribucionPorROL")
    @PreAuthorize("hasAuthority('METRICAS_SISTEMA')")
    public ResponseEntity<?> distribucionUsuariosPorRol(@RequestBody FiltroFechasDTO filtroFechasDTO) {
        DistribucionUsuariosPorRolResponseDTO distribucionUsuariosPorRolResponseDTO = metricasService.distribucionUsuariosPorRol(filtroFechasDTO.getFechaDesde(), filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(distribucionUsuariosPorRolResponseDTO);
    }
    @Operation(summary = "Ver distribución de usuarios por pais")
    @PutMapping("/admin/usuariosPorPais")
    @PreAuthorize("hasAuthority('METRICAS_SISTEMA')")
    public ResponseEntity<?> usuariosPorPais(@RequestBody FiltroFechasDTO filtroFechasDTO) {
        List<UsuariosPorPaisDTO> usuarios = metricasService.cantidadUsuariosPorPaisTop5(filtroFechasDTO.getFechaDesde(), filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(usuarios);
    }

    @Operation(summary = "Ver top 5 empresas con más ofertas creadas")
    @PutMapping("/admin/empresasConMasOfertas")
    @PreAuthorize("hasAuthority('METRICAS_SISTEMA')")
    public ResponseEntity<?> empresasConMasOfertas(@RequestBody FiltroFechasDTO filtroFechasDTO) {
        List<EmpresasConMasOfertasDTO> empresas = metricasService.topEmpresasConMasOfertas(filtroFechasDTO.getFechaDesde(), filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(empresas);
    }

    @Operation(summary = "Ver usuarios registrados a través del tiempo")
    @PutMapping("/admin/usuariosRegistrados")
    @PreAuthorize("hasAuthority('METRICAS_SISTEMA')")
    public ResponseEntity<?> usuariosRegistrados(@RequestBody FiltroFechasDTO filtroFechasDTO) {
        List<EvolucionUsuariosDTO> ev = metricasService.evolucionUsuariosRegistrados(filtroFechasDTO.getFechaDesde(), filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(ev);
    }

    
    //CANDIDATOS
    @Operation(summary = "Cantidad total de postulaciones en curso")
    @PutMapping("/candidato/enCurso/{idCandidato}")
    @PreAuthorize("hasAuthority('METRICAS_CANDIDATO')")
    public ResponseEntity<?> cantidadPostulacionesEnCurso(@PathVariable Long idCandidato) {
        Long cantidad = metricasService.contarPostulacionesEnCurso(idCandidato);
        return ResponseEntity.ok().body(Map.of("postulacionesEnCurso", cantidad));
    }

    @Operation(summary = "Cantidad total de postulaciones rechazadas")
    @PutMapping("/candidato/rechazadas/{idCandidato}")
    @PreAuthorize("hasAuthority('METRICAS_CANDIDATO')")
    public ResponseEntity<?> cantidadPostulacionesRechazadas(@PathVariable Long idCandidato, @RequestBody FiltroFechasDTO filtroFechasDTO) {
        Long cantidad = metricasService.contarPostulacionesRechazadas(idCandidato, filtroFechasDTO.getFechaDesde(), filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(Map.of("postulacionesRechazadas", cantidad));
    }

    @Operation(summary = "Ver rubros de interes del candidato")
    @PutMapping("/candidato/rubrosDeInteres/{idCandidato}")
    @PreAuthorize("hasAuthority('METRICAS_CANDIDATO')")
    public ResponseEntity<?> verRubrosDeInteres(@PathVariable Long idCandidato, @RequestBody FiltroFechasDTO filtroFechasDTO) {
        List<RubrosDeInteresDTO> rubros = metricasService.verRubrosDeInteres(idCandidato, filtroFechasDTO.getFechaDesde(), filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(rubros);
    }

    @Operation(summary = "Ver paises más postulados del candidato")
    @PutMapping("/candidato/paisesMasPostulados/{idCandidato}")
    @PreAuthorize("hasAuthority('METRICAS_CANDIDATO')")
    public ResponseEntity<?> verPaisesMasPostulados(@PathVariable Long idCandidato, @RequestBody FiltroFechasDTO filtroFechasDTO) {
        DistribucionPostulacionesPorPaisDTO distribucion = metricasService.verPaisesMasPostulados(idCandidato, filtroFechasDTO.getFechaDesde(), filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(distribucion);
    }

    @Operation(summary = "Ver top 3 habilidades blandas")
    @PutMapping("/candidato/habilidadesBlandas")
    @PreAuthorize("hasAuthority('METRICAS_CANDIDATO')")
    public ResponseEntity<?> verTopHabilidadesBlandas(@RequestBody FiltroFechasDTO filtroFechasDTO) {
        List<TopHabilidadDTO> habilidades = metricasService.topHabilidadesBlandas(filtroFechasDTO.getFechaDesde(),filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(habilidades);
    }

    @Operation(summary = "Ver top 3 habilidades tecnicas")
    @PutMapping("/candidato/habilidadesTecnicas")
    @PreAuthorize("hasAuthority('METRICAS_CANDIDATO')")
    public ResponseEntity<?> verTopHabilidadesTecnicas(@RequestBody FiltroFechasDTO filtroFechasDTO) {
        List<TopHabilidadDTO> habilidades = metricasService.topHabilidadesTecnicas(filtroFechasDTO.getFechaDesde(),filtroFechasDTO.getFechaHasta());
        return ResponseEntity.ok().body(habilidades);
    }

    //EMPRESAS
}
