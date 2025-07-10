package com.example.demo.repositories.params;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.params.Provincia;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface ProvinciaRepository extends BaseRepository<Provincia, Long> {
    
    @Query(
        value = "SELECT * FROM provincia p WHERE p.id_pais = :idPais " + 
                "ORDER BY p.nombre_provincia ASC ",
        nativeQuery = true
    )
    List<Provincia> findProvinciaByPaisId(@Param("idPais") Long idPais);
}
