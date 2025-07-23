package com.example.demo.services.empresa;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entities.empresa.EmpleadoEmpresa;
import com.example.demo.entities.empresa.Empresa;
import com.example.demo.services.seguridad.UsuarioService;

import jakarta.transaction.Transactional;

@Service
public class EmpresaOrquestadorService {
    private final EmpresaService empresaService;
    private final EmpleadoEmpresaService empleadoEmpresaService;
    private final UsuarioService usuarioService;

    public EmpresaOrquestadorService(EmpresaService empresaService, EmpleadoEmpresaService empleadoEmpresaService, UsuarioService usuarioService){
        this.empresaService = empresaService;
        this.empleadoEmpresaService = empleadoEmpresaService;
        this.usuarioService = usuarioService;
    }

    
    @Transactional
    public void darDeBajaEmpresaYRelacionados(Long idEmpresa){
        Empresa empresa = empresaService.findById(idEmpresa);
        List<EmpleadoEmpresa> empleados = empleadoEmpresaService.visualizarEmpleados(idEmpresa);
        
        for(EmpleadoEmpresa empleado: empleados){
            empleado.setFechaHoraBaja(new Date());
            empleadoEmpresaService.save(empleado);
            usuarioService.darDeBajaUsuario(empleado.getUsuario().getId()); 
        }
        usuarioService.darDeBajaUsuario(empresa.getUsuario().getId());
        empresa.setFechaHoraBaja(new Date());
        empresaService.save(empresa);
        
    }
}
