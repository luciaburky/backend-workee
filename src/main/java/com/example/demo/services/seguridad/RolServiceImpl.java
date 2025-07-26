package com.example.demo.services.seguridad;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.RolRequestDTO;
import com.example.demo.entities.seguridad.CategoriaRol;
import com.example.demo.entities.seguridad.Permiso;
import com.example.demo.entities.seguridad.PermisoRol;
import com.example.demo.entities.seguridad.Rol;
import com.example.demo.exceptions.EntityAlreadyEnabledException;
import com.example.demo.exceptions.EntityAlreadyExistsException;
import com.example.demo.exceptions.EntityNotValidException;
import com.example.demo.exceptions.EntityReferencedException;
import com.example.demo.repositories.seguridad.RolRepository;
import com.example.demo.services.BaseServiceImpl;

import jakarta.transaction.Transactional;

@Service
public class RolServiceImpl extends BaseServiceImpl<Rol, Long> implements RolService{
    private final RolRepository rolRepository;
    private final CategoriaRolService categoriaRolService;
    private final PermisoService permisoService;
    private final UsuarioRolService usuarioRolService;
    
    public RolServiceImpl(RolRepository rolRepository, CategoriaRolService categoriaRolService, PermisoService permisoService, UsuarioRolService usuarioRolService){
        super(rolRepository);
        this.rolRepository = rolRepository;
        this.categoriaRolService = categoriaRolService;
        this.permisoService = permisoService;
        this.usuarioRolService = usuarioRolService;
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

    @Override
    @Transactional
    public Rol modificarRol(RolRequestDTO rolRequestDTO, Long idRol){
        Rol rolExistente = findById(idRol);

        if(!rolRequestDTO.getNombreRol().isBlank() && rolRequestDTO.getNombreRol() != null){
            if(rolRepository.existsByNombreRol(rolRequestDTO.getNombreRol()) && !rolRequestDTO.getNombreRol().equals(rolExistente.getNombreRol())){
                throw new EntityAlreadyExistsException("Ya existe un rol con el nombre ingresado");
            }
            rolExistente.setNombreRol(rolRequestDTO.getNombreRol());
        }

        if (rolRequestDTO.getIdPermisos() == null || rolRequestDTO.getIdPermisos().isEmpty()) {
            throw new EntityNotValidException("Se debe seleccionar al menos un permiso");
        }

        List<PermisoRol> actuales = rolExistente.getPermisoRolList();
        Set<Long> idsNuevos = new HashSet<>(rolRequestDTO.getIdPermisos());

        //Dar de baja los que no selecciono que ya tenia
        for(PermisoRol permisoRolViejo: actuales){
            Long idPermisoViejo = permisoRolViejo.getPermiso().getId();
            if(!idsNuevos.contains(idPermisoViejo) && permisoRolViejo.getFechaHoraBaja() == null){
                permisoRolViejo.setFechaHoraBaja(new Date());
            }
        }

        //Habilitar los que estaban dados de baja
        for(PermisoRol permisoRolViejo: actuales){
            Long idPermiso = permisoRolViejo.getPermiso().getId();
            if(idsNuevos.contains(idPermiso) && permisoRolViejo.getFechaHoraBaja() != null){
                permisoRolViejo.setFechaHoraBaja(null);
            }
        }
       
        //Agregar los nuevos que selecciono
        Set<Long> idsActuales = actuales.stream()
            .map(permisoRol -> permisoRol.getPermiso().getId())
            .collect(Collectors.toSet());
        for(Long idPermiso : idsNuevos){
            if(!idsActuales.contains(idPermiso)){
                Permiso permiso = permisoService.findById(idPermiso);
                PermisoRol nuevoPermisoRol = new PermisoRol();
                nuevoPermisoRol.setFechaHoraAlta(new Date());
                nuevoPermisoRol.setPermiso(permiso);
                actuales.add(nuevoPermisoRol);
            }
        }

        return rolRepository.save(rolExistente);
    }

    @Override
    @Transactional
    public Boolean deshabilitarRol(Long idRol){
        if(idRol == null){
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        Boolean rolEstaEnUso = usuarioRolService.existenUsuariosActivosUsandoRol(idRol);
        if(rolEstaEnUso){
            throw new EntityReferencedException("No se puede deshabilitar el rol porque se encuentra en uso.");
        }
        return delete(idRol);
    }

    @Override
    @Transactional
    public Boolean habilitarRol(Long idRol){
        if(idRol == null){
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        Rol rol = findById(idRol);
        if(rol.getFechaHoraBaja() == null){
            throw new EntityAlreadyEnabledException("El rol ya está habilitado");
        }
        rol.setFechaHoraBaja(null);
        rolRepository.save(rol);
        return true;
    }
}
