package com.example.demo.repositories.seguridad;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.seguridad.Permiso;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface PermisoRepository extends BaseRepository<Permiso, Long> {
    
    @Query(
        value = "SELECT p.* FROM permiso p " + 
            "INNER JOIN categoria_rol_permiso crp ON p.id = crp.id_permiso " + 
            "WHERE crp.id_categoria_rol = :idCategoriaRol",
        nativeQuery = true
    )
    List<Permiso> buscarPermisosPorCategoria(@Param("idCategoriaRol") Long idCategoriaRol);

    @Query(
        value = "SELECT p.* FROM permiso p " + 
            "INNER JOIN permiso_rol pr ON p.id = pr.id_permiso " + 
            "WHERE pr.id_rol = :idRol",
        nativeQuery = true
    )
    List<Permiso> buscarPermisosPorRol(@Param("idRol") Long idRol);
}
