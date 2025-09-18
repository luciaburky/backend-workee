package com.example.demo.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entities.candidato.Candidato;
import com.example.demo.entities.empresa.EmpleadoEmpresa;
import com.example.demo.entities.empresa.Empresa;
import com.example.demo.entities.seguridad.Usuario;
import com.example.demo.exceptions.EntityAlreadyDisabledException;
import com.example.demo.services.candidato.CandidatoService;
import com.example.demo.services.empresa.EmpleadoEmpresaService;
import com.example.demo.services.empresa.EmpresaService;
import com.example.demo.services.seguridad.UsuarioService;

import jakarta.transaction.Transactional;

@Service
public class BajaOrquestadorServiceImpl implements BajaOrquestadorService{
    private final EmpresaService empresaService;
    private final EmpleadoEmpresaService empleadoEmpresaService;
    private final UsuarioService usuarioService;
    private final CandidatoService candidatoService;

    public BajaOrquestadorServiceImpl(EmpresaService empresaService, EmpleadoEmpresaService empleadoEmpresaService, UsuarioService usuarioService, CandidatoService candidatoService){
        this.empresaService = empresaService;
        this.empleadoEmpresaService = empleadoEmpresaService;
        this.usuarioService = usuarioService;
        this.candidatoService = candidatoService;
    }

    @Override
    @Transactional
    public void darDeBajaEmpresaYRelacionados(Long idEmpresa){
        Empresa empresa = empresaService.findById(idEmpresa);
        List<EmpleadoEmpresa> empleados = empleadoEmpresaService.visualizarTodosLosEmpleadosDeUnaEmpresa(idEmpresa);
        
        for(EmpleadoEmpresa empleado: empleados){
            empleado.setFechaHoraBaja(new Date());
            empleadoEmpresaService.save(empleado);
            usuarioService.darDeBajaUsuario(empleado.getUsuario().getId()); 
        }
        usuarioService.darDeBajaUsuario(empresa.getUsuario().getId());
        empresa.setFechaHoraBaja(new Date());
        empresaService.save(empresa);
    }

    @Override
    @Transactional
    public void darDeBajaUsuarioYEntidadRelacionada(Long idUsuario){
        Usuario usuario = usuarioService.findById(idUsuario);

        if(usuario.getFechaHoraBaja() != null){
            throw new EntityAlreadyDisabledException("El usuario ya se encuentra dado de baja");
        }

        Optional<Empresa> empresaOptional = empresaService.buscarEmpresaPorIdUsuario(idUsuario);
        if(empresaOptional.isPresent()){
            darDeBajaEmpresaYRelacionados(empresaOptional.get().getId());
            return;
        }

        Optional<EmpleadoEmpresa> empleadoEmpresaOptional = empleadoEmpresaService.buscarEmpleadoEmpresaPorUsuarioId(idUsuario);
        if(empleadoEmpresaOptional.isPresent()){
            EmpleadoEmpresa empleado = empleadoEmpresaOptional.get();
            empleado.setFechaHoraBaja(new Date());
            empleadoEmpresaService.save(empleado);
            usuarioService.darDeBajaUsuario(usuario.getId());
            return;
        }

        Optional<Candidato> candidatoOptional = candidatoService.buscarCandidatoPorIdUsuario(idUsuario);
        if(candidatoOptional.isPresent()){
            Candidato candidato = candidatoOptional.get();
            candidato.setFechaHoraBaja(new Date());
            candidatoService.save(candidato);
            usuarioService.darDeBajaUsuario(usuario.getId());
            return;
        }
    }
}
