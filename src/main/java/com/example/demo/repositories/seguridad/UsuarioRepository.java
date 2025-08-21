package com.example.demo.repositories.seguridad;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.seguridad.Usuario;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface UsuarioRepository extends BaseRepository<Usuario, Long>{
    
    public Boolean existsByFechaHoraBajaIsNullAndCorreoUsuarioLike(String correo);

    @Query(
        value = "SELECT * FROM usuario " + 
            "WHERE correo_usuario LIKE :correo " + 
            "AND fecha_hora_baja IS NULL",
        nativeQuery = true
    )
    public Optional<Usuario> buscarUsuarioPorCorreo(@Param("correo") String correo);
    
}

