package com.example.demo.repositories.params;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.params.Habilidad;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface HabilidadRepository extends BaseRepository<Habilidad, Long> {
    List<Habilidad> findByTipoHabilidadId(Long idTipoHabilidad);
}
