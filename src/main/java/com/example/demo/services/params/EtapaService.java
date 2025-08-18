package com.example.demo.services.params;

import java.util.Collection;
import java.util.List;

import com.example.demo.dtos.params.EtapaRequestDTO;
import com.example.demo.entities.params.Etapa;
import com.example.demo.services.BaseService;

public interface EtapaService extends BaseService<Etapa, Long> {

    Etapa crearPredeterminada(EtapaRequestDTO dto);

    Etapa crearPropia(Long empresaId, EtapaRequestDTO dto);

    Etapa actualizarEtapa(Long id, EtapaRequestDTO etapaDTO);

    Boolean habilitarEtapa(Long id);

    void deshabilitarEtapa(Long etapaId);

    //Ver si es util
    List<Etapa> obtenerEtapas();

    //Ver si es util
    List<Etapa> obtenerEtapasActivos();

    //TODO 
    /* 
    List<Etapa> findDisponiblesParaEmpresa(Long empresaId);

    List<Etapa> findAllByIdIn(Collection<Long> ids); //para OfertaEtapa

    void eliminarEtapaPropia(Long idEtapa);
    
    */
}
