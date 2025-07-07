package com.example.demo.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.params.Habilidad;

@Repository
public interface HabilidadRepository extends BaseRepository<Habilidad, Long> {
    List<Habilidad> findByTipoHabilidadId(Long idTipoHabilidad);
}
