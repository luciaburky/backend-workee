package com.example.demo.repositories.params;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.params.Pais;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface PaisRepository extends BaseRepository<Pais, Long> {
    Optional<Pais> findByNombrePaisIgnoreCase(String nombrePais);
}
