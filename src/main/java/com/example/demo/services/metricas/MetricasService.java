package com.example.demo.services.metricas;

import java.time.LocalDateTime;

public interface MetricasService {
    public Integer cantidadTotalHistoricaUsuarios(); //En la HU decía que a este no se le aplican los filtros de fechas

    public Double tasaExitoOfertas(LocalDateTime fechaDesde, LocalDateTime fechaHasta);
}
