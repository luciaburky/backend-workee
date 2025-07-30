package com.example.demo.repositories.seguridad;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dtos.UsuarioResponseDTO;
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

    @Query(
        """
             SELECT new com.example.demo.dtos.UsuarioResponseDTO(ur.usuario.id, ur.usuario.urlFotoUsuario, ur.usuario.correoUsuario, ur.rol.nombreRol)  
             FROM UsuarioRol ur
             WHERE ur.usuario.fechaHoraBaja IS NULL
             AND ur.fechaHoraBaja IS NULL
        """
    )
    public List<UsuarioResponseDTO> buscarUsuariosActivos();

    @Query(
        """
             SELECT new com.example.demo.dtos.UsuarioResponseDTO(ur.usuario.id, ur.usuario.urlFotoUsuario, ur.usuario.correoUsuario, ur.rol.nombreRol)  
             FROM UsuarioRol ur
             WHERE ur.usuario.fechaHoraBaja IS NULL
             AND ur.fechaHoraBaja IS NULL
             AND ur.rol.nombreRol LIKE :nombreRol
        """
    )
    public List<UsuarioResponseDTO> buscarUsuariosActivosPorRol(String nombreRol);


    @Query(
        value = "SELECT * FROM usuario_rol " + 
            "WHERE fecha_hora_baja IS NULL " + 
            "AND id_usuario = :idUsuario",
        nativeQuery = true
    )
    public Optional<UsuarioRol> buscarUsuarioRolActualSegunIdUsuario(Long idUsuario);

    @Query(
        value = "SELECT * FROM usuario_rol " +  
            "WHERE id_usuario = :idUsuario AND id_rol = :idRol",
        nativeQuery = true
    )
    public Optional<UsuarioRol> buscarUsuarioRolAnteriorSegunIdUsuarioEIdRol(Long idRol, Long idUsuario);
}

