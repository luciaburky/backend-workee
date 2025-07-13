package com.example.demo.services.params;

import java.util.List;

import com.example.demo.dtos.params.EstadoBusquedaRequestDTO;
import com.example.demo.entities.params.EstadoBusqueda;
import com.example.demo.services.BaseService;

public interface EstadoBusquedaService extends BaseService<EstadoBusqueda, Long> {
    
    EstadoBusqueda guardarEstadoBusqueda(EstadoBusquedaRequestDTO estadoBusquedaDTO);
    
    EstadoBusqueda actualizarEstadoBusqueda(Long id, EstadoBusquedaRequestDTO estadoBusquedaDTO);
    
    Boolean habilitarEstadoBusqueda(Long id);
    
    List<EstadoBusqueda> obtenerEstadosBusqueda();

    List<EstadoBusqueda> obtenerEstadosBusquedaActivos();
}
