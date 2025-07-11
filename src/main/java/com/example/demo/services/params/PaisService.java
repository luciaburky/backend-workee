package com.example.demo.services.params;

import java.util.List;

import com.example.demo.dtos.params.PaisRequestDTO;
import com.example.demo.entities.params.Pais;
import com.example.demo.entities.params.Provincia;
import com.example.demo.services.BaseService;

public interface PaisService extends BaseService<Pais, Long> {
    public Pais guardarPais(PaisRequestDTO paisRequestDTO);
}
