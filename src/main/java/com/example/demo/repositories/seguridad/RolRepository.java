package com.example.demo.repositories.seguridad;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.seguridad.Rol;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface RolRepository extends BaseRepository<Rol, Long>{
    List<Rol> findAllByOrderByNombreRolAsc();
}
