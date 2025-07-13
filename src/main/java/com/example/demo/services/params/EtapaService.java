package com.example.demo.services.params;

import java.util.List;

import com.example.demo.dtos.params.EtapaRequestDTO;
import com.example.demo.entities.params.Etapa;
import com.example.demo.services.BaseService;

public interface EtapaService extends BaseService<Etapa, Long> {

    Etapa guardarEtapa(EtapaRequestDTO etapaDTO);

    Etapa actualizarEtapa(Long id, EtapaRequestDTO etapaDTO);

    Boolean habilitarEtapa(Long id);

    List<Etapa> obtenerEtapas();

    List<Etapa> obtenerEtapasActivos();

}
