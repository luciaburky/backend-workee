package com.example.demo.services.params;

import java.util.List;

import com.example.demo.dtos.params.EstadoUsuarioRequestDTO;
import com.example.demo.entities.params.EstadoUsuario;
import com.example.demo.services.BaseService;

public interface EstadoUsuarioService extends BaseService<EstadoUsuario, Long>{

    public EstadoUsuario guardarEstadoUsuario(EstadoUsuarioRequestDTO estadoUsuarioRequestDTO);

    public EstadoUsuario actualizarEstadoUsuario(Long id, EstadoUsuarioRequestDTO estadoUsuarioRequestDTO);

    public EstadoUsuario buscarEstadoUsuarioPorId(Long id); //TODO: Si usamos el de base lo borramos

    public List<EstadoUsuario> obtenerEstadosUsuario();

    public List<EstadoUsuario> obtenerEstadosUsuarioActivos();

    public Boolean habilitarEstadoUsuario(Long id);

    public Boolean deshabilitarEstadoUsuario(Long id); //TODO: Si usamos el de base lo borramos
}
