package com.example.demo.controllers.metricas;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.metricas.admin.DistribucionUsuariosPorRolResponseDTO;
import com.example.demo.dtos.metricas.admin.EmpresasConMasOfertasDTO;
import com.example.demo.dtos.metricas.admin.FiltroFechasDTO;
import com.example.demo.dtos.metricas.admin.UsuariosPorPaisDTO;
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

    //EMPRESAS

    //CANDIDATOS

}
