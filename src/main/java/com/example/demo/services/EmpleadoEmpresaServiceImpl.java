package com.example.demo.services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.EmpleadoEmpresaRequestDTO;
import com.example.demo.entities.EmpleadoEmpresa;
import com.example.demo.entities.Empresa;
import com.example.demo.exceptions.EntityNotValidException;
import com.example.demo.mappers.EmpleadoEmpresaMapper;
import com.example.demo.repositories.EmpleadoEmpresaRepository;

import jakarta.transaction.Transactional;

@Service
public class EmpleadoEmpresaServiceImpl extends BaseServiceImpl<EmpleadoEmpresa, Long> implements EmpleadoEmpresaService{
  
    private final EmpleadoEmpresaRepository empleadoEmpresaRepository;
    private final EmpresaService empresaService;
    private final EmpleadoEmpresaMapper empleadoEmpresaMapper;

    public EmpleadoEmpresaServiceImpl(EmpleadoEmpresaRepository empleadoEmpresaRepository, EmpresaService empresaService, EmpleadoEmpresaMapper empleadoEmpresaMapper) {
        super(empleadoEmpresaRepository);
        this.empleadoEmpresaRepository = empleadoEmpresaRepository;
        this.empresaService = empresaService;
        this.empleadoEmpresaMapper = empleadoEmpresaMapper;
    }
    
    @Override
    @Transactional
    public EmpleadoEmpresa darDeAltaEmpleado(EmpleadoEmpresaRequestDTO empleadoEmpresaRequestDTO){
        System.out.println("El id de la empresa es " + empleadoEmpresaRequestDTO.getIdEmpresa());
        if(empleadoEmpresaRequestDTO.getIdEmpresa() == null){
            throw new IllegalArgumentException("El id de la empresa no puede estar vacío");
        }
        if(!empleadoEmpresaRequestDTO.getRepetirContrasenia().equals(empleadoEmpresaRequestDTO.getContrasenia()) ){
            throw new EntityNotValidException("Las contraseñas deben coincidir");
        }
        Empresa empresa = empresaService.findById(empleadoEmpresaRequestDTO.getIdEmpresa());
        EmpleadoEmpresa nuevoEmpleado = new EmpleadoEmpresa();

        nuevoEmpleado = empleadoEmpresaMapper.toEntity(empleadoEmpresaRequestDTO);
        nuevoEmpleado.setEmpresa(empresa);
        nuevoEmpleado.setFechaHoraAlta(new Date());

        empleadoEmpresaRepository.save(nuevoEmpleado);

        //TODO: agregar validacion de que no exista el correo electronico
        return nuevoEmpleado;
    }
   

    @Override
    @Transactional
    public EmpleadoEmpresa modificarEmpleado(EmpleadoEmpresaRequestDTO empleadoEmpresaRequestDTO, boolean esEmpleadoModificandoseASiMismo, Long id){
        EmpleadoEmpresa empleadoOriginal = findById(id);

        if(esEmpleadoModificandoseASiMismo){
            modificarComoEmpleado(empleadoOriginal, empleadoEmpresaRequestDTO);
        } else {
            modificarComoAdministrador(empleadoOriginal, empleadoEmpresaRequestDTO);
        }

        return empleadoEmpresaRepository.save(empleadoOriginal);
    }

    private void modificarComoEmpleado(EmpleadoEmpresa empleadoEmpresa, EmpleadoEmpresaRequestDTO empleadoEmpresaRequestDTO){
        empleadoEmpresaMapper.updateEntityFromDto(empleadoEmpresaRequestDTO, empleadoEmpresa);
        //Según el chat, cuando agreguemos lo del modulo de seguridad lo mejor es manejarlo con el PreAuthorize (iria en el controller). Ejemplo:
        /*
         * @PreAuthorize("hasRole('EMPLEADO') and #id == authentication.principal.id")
            @PutMapping("/modificar-propio/{id}")
            public ResponseEntity<?> modificarEmpleadoPropio(...) {
            ...
            y en el service (si usamos un unico metodo): 
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String email = auth.getName();
            }
         */

    }

    private void modificarComoAdministrador(EmpleadoEmpresa empleadoEmpresa, EmpleadoEmpresaRequestDTO empleadoEmpresaRequestDTO){
        if(empleadoEmpresaRequestDTO.getPuestoEmpleadoEmpresa() != null && !empleadoEmpresaRequestDTO.getPuestoEmpleadoEmpresa().isEmpty()){
            empleadoEmpresa.setPuestoEmpleadoEmpresa(empleadoEmpresaRequestDTO.getPuestoEmpleadoEmpresa());
        } else {
            throw new EntityNotValidException("El puesto no puede ser nulo");
        }
    }

    
    public Boolean darDeBajaEmpleadoEmpresa(Long id){
        //TODO: Agregar validacion de que si esta asociado a ofertas, q esten todas finalizadas

        return delete(id);
    }


    @Override
    public List<EmpleadoEmpresa> visualizarEmpleados(Long idEmpresa){
        return empleadoEmpresaRepository.traerEmpleadosActivos(idEmpresa);
    }

    @Override
    public Long contarEmpleadosDeEmpresa(Long idempresa){
        return empleadoEmpresaRepository.contarEmpelados(idempresa);
    }
}


