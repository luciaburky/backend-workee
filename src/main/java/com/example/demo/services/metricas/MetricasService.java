package com.example.demo.services.metricas;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.dtos.metricas.DistribucionUsuariosPorRolResponseDTO;
import com.example.demo.dtos.metricas.UsuariosPorPaisDTO;

public interface MetricasService {
    public Integer cantidadTotalHistoricaUsuarios(); //En la HU decía que a este no se le aplican los filtros de fechas

    public Double tasaExitoOfertas(LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    public DistribucionUsuariosPorRolResponseDTO distribucionUsuariosPorRol(LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    public List<UsuariosPorPaisDTO> cantidadUsuariosPorPaisTop5(LocalDateTime fechaDesde, LocalDateTime fechaHasta);
}
