package com.example.demo.repositories.candidato;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.candidato.Candidato;
import com.example.demo.repositories.BaseRepository;

public interface CandidatoRepository extends BaseRepository<Candidato, Long> {
    
    List<Candidato> findAllByOrderByNombreCandidatoAsc();
    
    @Query("SELECT c FROM Candidato c JOIN FETCH c.habilidades ch LEFT JOIN FETCH ch.habilidad WHERE c.id = :id")
    Optional<Candidato> findByIdWithHabilidades(@Param("id") Long id);
}
