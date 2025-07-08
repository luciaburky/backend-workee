package com.example.demo.services.params;

import java.util.List;

import com.example.demo.entities.params.Provincia;
import com.example.demo.services.BaseService;

public interface ProvinciaService extends BaseService<Provincia, Long>{
    public List<Provincia> findProvinciaByPaisId(Long idPais);
}
