package com.example.demo.services.seguridad;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.UsuarioDTO;
import com.example.demo.entities.seguridad.Usuario;
import com.example.demo.exceptions.EntityAlreadyExistsException;
import com.example.demo.mappers.UsuarioMapper;
import com.example.demo.repositories.seguridad.UsuarioRepository;
import com.example.demo.services.BaseServiceImpl;

import jakarta.transaction.Transactional;

@Service
public class UsuarioServiceImpl extends BaseServiceImpl<Usuario, Long> implements UsuarioService{

    private final UsuarioRepository usuarioRepository;
    //private final BCrypt
    private final UsuarioMapper usuarioMapper;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        
    }


    @Override
    @Transactional
    public Usuario registrarUsuario(UsuarioDTO usuarioDTO){
        if(existeUsuarioConCorreo(usuarioDTO.getCorreoUsuario())){
            throw new EntityAlreadyExistsException("El correo ingresado ya se encuentra en uso");
        }

        String contraseniaCodificada = codificarContrasenia(usuarioDTO.getContraseniaUsuario());
        
        Usuario nuevoUsuario = usuarioMapper.toEntity(usuarioDTO);//new Usuario();
        nuevoUsuario.setContraseniaUsuario(contraseniaCodificada);
        return usuarioRepository.save(nuevoUsuario);
        
    }

    private String codificarContrasenia(String contrasenia){
        return contrasenia; //TODO: Hacer codificacion de contraseña 
    }
    
    private Boolean existeUsuarioConCorreo(String correoUsuario){
        return usuarioRepository.existsByFechaHoraBajaIsNullAndCorreoUsuarioLike(correoUsuario);
    }
}

