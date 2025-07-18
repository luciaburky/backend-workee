package com.example.demo.repositories;

import java.util.List;

import com.example.demo.entities.Candidato;

public interface CandidatoRepository extends BaseRepository<Candidato, Long> {
    
    List<Candidato> findAllByOrderByNombreCandidatoAsc();
}
