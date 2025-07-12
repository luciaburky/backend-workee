package com.example.demo.services;

import java.util.Date;

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

        return nuevoEmpleado;
    }
   

    //TODO: agregar validacion de que no exista el correo electronico
}


