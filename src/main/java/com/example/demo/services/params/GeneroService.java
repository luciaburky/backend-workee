package com.example.demo.services.params;
import java.util.List;

import com.example.demo.dtos.params.GeneroRequestDTO;
import com.example.demo.entities.params.Genero;
import com.example.demo.services.BaseService;

public interface GeneroService extends BaseService<Genero, Long>{
    public Genero guardarGenero(GeneroRequestDTO GeneroRequestDTO);

    public Genero actualizarGenero(Long id, GeneroRequestDTO GeneroRequestDTO);

    public Genero buscarGeneroPorId(Long id); //TODO: Si usamos el de base lo borramos

    public List<Genero> obtenerGeneros();

    public List<Genero> obtenerGenerosActivos();

    public Boolean habilitarGenero(Long id);

    public Boolean deshabilitarGenero(Long id); //TODO: Si usamos el de base lo borramos
}
