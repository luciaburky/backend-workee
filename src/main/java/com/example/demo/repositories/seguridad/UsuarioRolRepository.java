package com.example.demo.repositories.seguridad;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.seguridad.UsuarioRol;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface UsuarioRolRepository extends BaseRepository<UsuarioRol, Long> {

    @Query(
        """
            SELECT COUNT(ur) > 0 FROM UsuarioRol ur
            WHERE ur.rol.id = :idRol 
            AND ur.fechaHoraBaja IS NULL
            AND ur.usuario.fechaHoraBaja IS NULL
        """
    )
    Boolean existenUsuariosActivosConRol(@Param("idRol") Long idRol);
}
