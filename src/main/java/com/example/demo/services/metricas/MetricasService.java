package com.example.demo.services.metricas;

import java.time.LocalDateTime;

import com.example.demo.dtos.metricas.admin.EstadisticasAdminDTO;
import com.example.demo.dtos.metricas.candidato.DistribucionPostulacionesPorPaisDTO;
import com.example.demo.dtos.metricas.candidato.EstadisticasCandidatoDTO;
import com.example.demo.dtos.metricas.empresa.DistribucionGenerosDTO;

public interface MetricasService {
    //ADMIN DEL SISTEMA
    public EstadisticasAdminDTO verEstadisticasAdminSistema(LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    //CANDIDATO
    public EstadisticasCandidatoDTO verEstadisticasCandidato(Long idCandidato, LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    //EMPRESA
    public Long obtenerCantidadOfertasAbiertas(Long idEmpresa);

    public DistribucionGenerosDTO distribucionGenerosEnOfertas(Long idEmpresa, LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    public Double tasaAbandonoOfertas(Long idEmpresa, LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    public Double tiempoPromedioContratacion(Long idEmpresa, LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    public DistribucionPostulacionesPorPaisDTO localizacionCandidatos(Long idEmpresa, LocalDateTime fechaDesde, LocalDateTime fechaHasta);
}
