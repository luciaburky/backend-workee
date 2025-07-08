package com.example.demo.services.params;
import com.example.demo.entities.params.Habilidad;
import com.example.demo.services.BaseService;

import java.util.List;

public interface HabilidadService extends BaseService<Habilidad, Long>{    
    //Buscar habilidad por tipo de habilidad
   List<Habilidad> findByTipoHabilidad(Long idTipoHabilidad);
}