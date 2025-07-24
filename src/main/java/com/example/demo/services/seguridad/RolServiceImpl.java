package com.example.demo.services.seguridad;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entities.seguridad.Rol;
import com.example.demo.repositories.seguridad.RolRepository;
import com.example.demo.services.BaseServiceImpl;

@Service
public class RolServiceImpl extends BaseServiceImpl<Rol, Long> implements RolService{
    private RolRepository rolRepository;
    
    public RolServiceImpl(RolRepository rolRepository){
        super(rolRepository);
    }

    @Override
    public List<Rol> obtenerRoles(){
        return rolRepository.findAllByOrderByNombreRolAsc();
    }

}
