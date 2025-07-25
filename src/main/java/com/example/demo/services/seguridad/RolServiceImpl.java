package com.example.demo.services.seguridad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.RolRequestDTO;
import com.example.demo.entities.seguridad.CategoriaRol;
import com.example.demo.entities.seguridad.Permiso;
import com.example.demo.entities.seguridad.PermisoRol;
import com.example.demo.entities.seguridad.Rol;
import com.example.demo.exceptions.EntityAlreadyExistsException;
import com.example.demo.exceptions.EntityNotValidException;
import com.example.demo.repositories.seguridad.RolRepository;
import com.example.demo.services.BaseServiceImpl;

import jakarta.transaction.Transactional;

@Service
public class RolServiceImpl extends BaseServiceImpl<Rol, Long> implements RolService{
    private final RolRepository rolRepository;
    private final CategoriaRolService categoriaRolService;
    private final PermisoService permisoService;
    
    public RolServiceImpl(RolRepository rolRepository, CategoriaRolService categoriaRolService, PermisoService permisoService){
        super(rolRepository);
        this.rolRepository = rolRepository;
        this.categoriaRolService = categoriaRolService;
        this.permisoService = permisoService;
    }

    @Override
    public List<Rol> obtenerRoles(){
        return rolRepository.findAllByOrderByNombreRolAsc();
    }

    @Override
    @Transactional
    public Rol crearRol(RolRequestDTO rolRequestDTO){
        boolean yaExisteRol = rolRepository.existsByNombreRol(rolRequestDTO.getNombreRol());
        if(yaExisteRol){
            throw new EntityAlreadyExistsException("Ya existe un rol con el nombre ingresado");
        }
        Rol nuevoRol = new Rol();
        nuevoRol.setFechaHoraAlta(new Date());
        nuevoRol.setNombreRol(rolRequestDTO.getNombreRol());
        
        if(rolRequestDTO.getIdCategoria() == null){
            throw new EntityNotValidException("Debe seleccionar una categoría");
        }

        CategoriaRol categoriaRol = categoriaRolService.findById(rolRequestDTO.getIdCategoria());
        nuevoRol.setCategoriaRol(categoriaRol);
        
        if(rolRequestDTO.getIdPermisos().isEmpty()){
            throw new EntityNotValidException("Debe seleccionar al menos un permiso");
        }

        nuevoRol.setPermisoRolList(new ArrayList<>());
        for(Long idPermiso: rolRequestDTO.getIdPermisos()){
            Permiso permiso = permisoService.findById(idPermiso);
            PermisoRol nuevoPermisoRol = new PermisoRol();
            nuevoPermisoRol.setPermiso(permiso);
            nuevoPermisoRol.setFechaHoraAlta(new Date());

            nuevoRol.getPermisoRolList().add(nuevoPermisoRol);
        }

        return rolRepository.save(nuevoRol);

        
    }

    

}
