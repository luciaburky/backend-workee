package com.example.demo.controllers.metricas;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @Operation(summary = "Ver cantidad histórica de usuarios")
    @GetMapping()
    @PreAuthorize("hasAuthority('METRICAS_SISTEMA')")
    public ResponseEntity<?> cantidadHistoricaUsuarios() {
        Integer cantidad = metricasService.cantidadTotalHistoricaUsuarios();
        return ResponseEntity.ok().body(Map.of("cantidadHistoricaUsuarios", cantidad));
    }


}
