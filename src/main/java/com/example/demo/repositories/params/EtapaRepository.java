package com.example.demo.repositories.params;

import java.lang.foreign.Linker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.params.Etapa;
import com.example.demo.repositories.BaseRepository;

public interface EtapaRepository extends BaseRepository<Etapa, Long> {
    
    List<Etapa> findAllByOrderByNombreEtapaAsc();

    @Query(value = "SELECT * FROM etapa WHERE fecha_hora_baja IS NULL " + 
            "ORDER BY nombre_etapa ASC", 
            nativeQuery = true)
    List<Etapa> buscarEtapasActivos();

    Optional<Etapa> findByNombreEtapaIgnoreCase(String nombreEtapa);
}
