package com.example.demo.repositories.seguridad;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.seguridad.Usuario;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface UsuarioRepository extends BaseRepository<Usuario, Long>{
    
    public Boolean existsByFechaHoraBajaIsNullAndCorreoUsuarioLike(String correo);

    public Usuario findByCorreoUsuarioAndFechaHoraBajaIsNull();
}

