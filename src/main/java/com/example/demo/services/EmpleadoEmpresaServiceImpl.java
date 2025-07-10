package com.example.demo.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.CrearEmpleadoEmpresaDTO;
import com.example.demo.dtos.ModificarEmpleadoEmpresaDTO;
import com.example.demo.entities.EmpleadoEmpresa;
import com.example.demo.entities.Empresa;
import com.example.demo.repositories.EmpleadoEmpresaRepository;

@Service
public class EmpleadoEmpresaServiceImpl extends BaseServiceImpl<EmpleadoEmpresa, Long> implements EmpleadoEmpresaService{
    private final EmpleadoEmpresaRepository empleadoEmpresaRepository;

    private final EmpresaService empresaService;

    public EmpleadoEmpresaServiceImpl(EmpleadoEmpresaRepository empleadoEmpresaRepository, EmpresaService empresaService) {
        super(empleadoEmpresaRepository);
        this.empleadoEmpresaRepository = empleadoEmpresaRepository;
        this.empresaService = empresaService;
    }
 
    public EmpleadoEmpresa crearEmpleadoEmpresa(CrearEmpleadoEmpresaDTO empleadoEmpresaRequestDTO) throws Exception {
        try{
            //TODO: cuando hayamos hecho el modulo de seguridad, descomentar esto
            // if(this.empleadoEmpresaRepository.existsByCorreo(empleadoEmpresaRequestDTO.getCorreo())){
            //     throw new Exception("Ya existe un empleado con ese correo");
            // }
            if (!empleadoEmpresaRequestDTO.getContrasenia().equals(empleadoEmpresaRequestDTO.getRepetirContrasenia())) {
                throw new IllegalArgumentException("Las contraseñas no coinciden");
            }
            EmpleadoEmpresa empleadoEmpresa = new EmpleadoEmpresa();
            
            empleadoEmpresa.setNombreEmpleadoEmpresa(empleadoEmpresaRequestDTO.getNombre());
            empleadoEmpresa.setApellidoEmpeladoEmpresa(empleadoEmpresaRequestDTO.getApellido());
            empleadoEmpresa.setFechaHoraAlta(new Date());
            empleadoEmpresa.setPuestoEmpleadoEmpresa(empleadoEmpresaRequestDTO.getPuesto());

            Empresa empresa; //TODO: ver si conviene separar esto en otro metodo, dependiendo de si se usa en otros lugares mas
            if(empleadoEmpresaRequestDTO.getIdEmpresa() != null){
                empresa = this.empresaService.findById(empleadoEmpresaRequestDTO.getIdEmpresa());
            } else {
                throw new IllegalArgumentException("El ID de la empresa no puede ser nulo");
            }
                   
            empleadoEmpresa.setEmpresa(empresa);

            //TODO: agregar lo de contraseña para el usuario
            return empleadoEmpresaRepository.save(empleadoEmpresa);
        } catch (IllegalArgumentException e) {
            throw new Exception("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Error al crear empleado: " + e.getMessage());
        }
    }

    public EmpleadoEmpresa actualizarEmpleado(ModificarEmpleadoEmpresaDTO modificarEmpleadoEmpresaDTO, Long idEmpleadoEmpresa) throws Exception {
        try{
            EmpleadoEmpresa empleadoEmpresaOriginal = empleadoEmpresaRepository.findById(idEmpleadoEmpresa)
                .orElseThrow(() -> new Exception("Empleado no encontrado"));

            if (modificarEmpleadoEmpresaDTO.getNombreEmpleadoEmpresa() != null) {
                empleadoEmpresaOriginal.setNombreEmpleadoEmpresa(modificarEmpleadoEmpresaDTO.getNombreEmpleadoEmpresa());
            }
            if (modificarEmpleadoEmpresaDTO.getApellidoEmpleadoEmpresa() != null) {
                empleadoEmpresaOriginal.setApellidoEmpeladoEmpresa(modificarEmpleadoEmpresaDTO.getApellidoEmpleadoEmpresa());
            }
            if (modificarEmpleadoEmpresaDTO.getPuestoEmpleadoEmpresa() != null) {
                empleadoEmpresaOriginal.setPuestoEmpleadoEmpresa(modificarEmpleadoEmpresaDTO.getPuestoEmpleadoEmpresa());
            }
            
            return empleadoEmpresaRepository.save(empleadoEmpresaOriginal);
        } catch (Exception e) {
            throw new Exception("Error al modificar el empleado: " + e.getMessage());
        }
    }

    //TODO: hacer las validaciones (en caso de quedar en eso)--> la de unico empleado y la de participacion en ofertas
    public Boolean darDeBajaEmpleado(Long idEmpleadoEmpresa) throws Exception {
        try {
            EmpleadoEmpresa empleadoEmpresa = empleadoEmpresaRepository.findById(idEmpleadoEmpresa)
                .orElseThrow(() -> new Exception("Empleado no encontrado"));
            
            empleadoEmpresa.setFechaHoraBaja(new Date());
            empleadoEmpresaRepository.save(empleadoEmpresa);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al dar de baja el empleado: " + e.getMessage());
        }
    }

    public Long contarEmpleadosActivos(Long idEmpresa) throws Exception {
        try{
            Long cantidadEmpleadosActivos = empleadoEmpresaRepository.contarEmpelados(idEmpresa);
            return cantidadEmpleadosActivos;
        } catch (Exception e) {
            throw new Exception("Error al contar empleados activos: " + e.getMessage());
        }
    }
}


