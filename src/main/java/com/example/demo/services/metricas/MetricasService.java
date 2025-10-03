package com.example.demo.services.metricas;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.dtos.metricas.admin.EstadisticasAdminDTO;
import com.example.demo.dtos.metricas.candidato.DistribucionPostulacionesPorPaisDTO;
import com.example.demo.dtos.metricas.candidato.RubrosDeInteresDTO;
import com.example.demo.dtos.metricas.candidato.TopHabilidadDTO;
import com.example.demo.dtos.metricas.empresa.DistribucionGenerosDTO;

public interface MetricasService {
    //ADMIN DEL SISTEMA
    public EstadisticasAdminDTO verEstadisticasAdminSistema(LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    //CANDIDATO
    public Long contarPostulacionesEnCurso(Long idCandidato);

    public Long contarPostulacionesRechazadas(Long idCandidato, LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    public List<RubrosDeInteresDTO> verRubrosDeInteres(Long idCandidato, LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    public DistribucionPostulacionesPorPaisDTO verPaisesMasPostulados(Long idCandidato, LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    public List<TopHabilidadDTO> topHabilidadesBlandas(LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    public List<TopHabilidadDTO> topHabilidadesTecnicas(LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    //EMPRESA
    public Long obtenerCantidadOfertasAbiertas(Long idEmpresa);

    public DistribucionGenerosDTO distribucionGenerosEnOfertas(Long idEmpresa, LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    public Double tasaAbandonoOfertas(Long idEmpresa, LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    public Double tiempoPromedioContratacion(Long idEmpresa, LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    public DistribucionPostulacionesPorPaisDTO localizacionCandidatos(Long idEmpresa, LocalDateTime fechaDesde, LocalDateTime fechaHasta);
}
