package com.example.demo.services.seguridad;

import com.example.demo.dtos.RecuperarContraseniaDTO;
import com.example.demo.dtos.UsuarioDTO;
import com.example.demo.entities.seguridad.Usuario;
import com.example.demo.services.BaseService;

public interface UsuarioService extends BaseService<Usuario, Long>{
    public Usuario registrarUsuario(UsuarioDTO usuarioDTO);

    public void actualizarDatosUsuario(Long idUsuario, String nuevaContrasenia, String repetirContrasenia, String nuevaUrlFoto);

    public void darDeBajaUsuario(Long idUsuario);

    public void solicitarRecuperarContrasenia(String correoUsuario);

    public void confirmarRecuperacionContrasenia(String idEncriptado, RecuperarContraseniaDTO recuperarContraseniaDTO);
}
