package com.example.demo.services.metricas;

import java.time.LocalDateTime;

import com.example.demo.dtos.metricas.DistribucionUsuariosPorRolResponseDTO;

public interface MetricasService {
    public Integer cantidadTotalHistoricaUsuarios(); //En la HU decía que a este no se le aplican los filtros de fechas

    public Double tasaExitoOfertas(LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    public DistribucionUsuariosPorRolResponseDTO distribucionUsuariosPorRol(LocalDateTime fechaDesde, LocalDateTime fechaHasta);
}
