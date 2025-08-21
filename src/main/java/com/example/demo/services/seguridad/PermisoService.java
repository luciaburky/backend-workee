package com.example.demo.services.seguridad;

import java.util.List;

import com.example.demo.entities.seguridad.Permiso;
import com.example.demo.services.BaseService;

public interface PermisoService extends BaseService<Permiso, Long>{
    public List<Permiso> obtenerPermisosDeUnaCategoria(Long idCategoria);
}
