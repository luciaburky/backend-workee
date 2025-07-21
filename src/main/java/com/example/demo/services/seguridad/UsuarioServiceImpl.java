package com.example.demo.services.seguridad;

import org.springframework.stereotype.Service;

import com.example.demo.entities.seguridad.Usuario;
import com.example.demo.repositories.seguridad.UsuarioRepository;
import com.example.demo.services.BaseServiceImpl;

@Service
public class UsuarioServiceImpl extends BaseServiceImpl<Usuario, Long> implements UsuarioService{

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        super(usuarioRepository);
        
    }

}

