package com.example.demo.services.seguridad;

import java.util.List;

import com.example.demo.dtos.RolRequestDTO;
import com.example.demo.entities.seguridad.Rol;
import com.example.demo.services.BaseService;

public interface RolService extends BaseService<Rol, Long>{
    public List<Rol> obtenerRoles();
    public Rol crearRol(RolRequestDTO rolRequestDTO);
    public Rol modificarRol(RolRequestDTO rolRequestDTO, Long idRol);
}
