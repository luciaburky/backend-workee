package com.example.demo.services.metricas;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.dtos.metricas.admin.DistribucionUsuariosPorRolResponseDTO;
import com.example.demo.dtos.metricas.admin.EmpresasConMasOfertasDTO;
import com.example.demo.dtos.metricas.admin.EvolucionUsuariosDTO;
import com.example.demo.dtos.metricas.admin.UsuariosPorPaisDTO;
import com.example.demo.dtos.metricas.candidato.DistribucionPostulacionesPorPaisDTO;
import com.example.demo.dtos.metricas.candidato.RubrosDeInteresDTO;
import com.example.demo.dtos.metricas.candidato.TopHabilidadDTO;
import com.example.demo.dtos.metricas.empresa.DistribucionGenerosDTO;

public interface MetricasService {
    //ADMIN DEL SISTEMA
    public Integer cantidadTotalHistoricaUsuarios(); //En la HU decía que a este no se le aplican los filtros de fechas

    public Double tasaExitoOfertas(LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    public DistribucionUsuariosPorRolResponseDTO distribucionUsuariosPorRol(LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    public List<UsuariosPorPaisDTO> cantidadUsuariosPorPaisTop5(LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    public List<EmpresasConMasOfertasDTO> topEmpresasConMasOfertas(LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    public List<EvolucionUsuariosDTO> evolucionUsuariosRegistrados(LocalDateTime fechaDesde, LocalDateTime fechaHasta);

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
