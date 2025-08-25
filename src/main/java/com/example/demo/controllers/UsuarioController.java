package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.FiltrosUsuariosRequestDTO;
import com.example.demo.dtos.UsuarioResponseDTO;
import com.example.demo.entities.seguridad.Usuario;
import com.example.demo.services.BajaOrquestadorService;
import com.example.demo.services.candidato.CandidatoService;
import com.example.demo.services.empresa.EmpleadoEmpresaService;
import com.example.demo.services.empresa.EmpresaService;
import com.example.demo.services.seguridad.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path ="/usuarios")
@Tag(name = "Usuario", description = "Controlador para operaciones de Usuario")

public class UsuarioController {
    private final UsuarioService usuarioService;
    private final BajaOrquestadorService bajaOrquestadorService;
    private final EmpleadoEmpresaService empleadoEmpresaService;
    private final EmpresaService empresaService;
    private final CandidatoService candidatoService;

    public UsuarioController(UsuarioService usuarioService, BajaOrquestadorService bajaOrquestadorService, EmpleadoEmpresaService empleadoEmpresaService, EmpresaService empresaService, CandidatoService candidatoService){
        this.usuarioService = usuarioService;
        this.bajaOrquestadorService = bajaOrquestadorService;
        this.empleadoEmpresaService = empleadoEmpresaService;
        this.empresaService = empresaService;
        this.candidatoService = candidatoService;
    }

    

    @Operation(summary = "Trae a todos los usuarios que se encuentran activos")
    @GetMapping("")
    @PreAuthorize("hasAuthority('VER_USUARIOS')") 
    public ResponseEntity<?> buscarUsuariosActivos(){
        List<UsuarioResponseDTO> usuarios = usuarioService.buscarUsuariosActivos();
        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }

    @Operation(summary = "Trae a todos los usuarios que se encuentran activos según el rol ingresado")
    @PutMapping("/porRol")
    @PreAuthorize("hasAuthority('VER_USUARIOS')") 
    public ResponseEntity<?> buscarUsuariosActivosPorRol(@Valid @RequestBody FiltrosUsuariosRequestDTO filtroUsuario){
        System.out.println("entreee");
        List<UsuarioResponseDTO> usuarios = usuarioService.buscarUsuariosActivosPorRol(filtroUsuario.getIdsRol());
        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }

    @Operation(summary = "Para dar de baja un usuario y su entidad relacionada")
    @DeleteMapping("/{idUsuario}")
    @PreAuthorize("hasAuthority('ELIMINAR_USUARIO')") 
    public ResponseEntity<?> darDeBajaUsuario(@PathVariable Long idUsuario){
        this.bajaOrquestadorService.darDeBajaUsuarioYEntidadRelacionada(idUsuario);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario dado de baja correctamente");
    }

    @Operation(summary = "Modificar el rol de un usuario")
    @PutMapping("/modificarRol/{idUsuario}")
    @PreAuthorize("hasAuthority('MODIFICAR_ROL_USUARIO')") 
    public ResponseEntity<?> modificarRolDeUsuario(@PathVariable Long idUsuario, @RequestBody Long idRol){
        usuarioService.modificarRolUsuario(idUsuario, idRol);
        return ResponseEntity.status(HttpStatus.OK).body("Rol modificado correctamente");
    }

    @Operation(summary = "Visualizar detalle de usuario")
    @GetMapping("/{idUsuario}")
    @PreAuthorize("hasAuthority('VER_DETALLE_USUARIO') or hasAuthority('MODIFICAR_ROL_USUARIO')") 
    public ResponseEntity<?> visualizarDetalleUsuario(@PathVariable Long idUsuario){
        UsuarioResponseDTO usuario = usuarioService.visualizarDetalleUsuario(idUsuario);
        return ResponseEntity.status(HttpStatus.OK).body(usuario);
    }

    @GetMapping("/miPerfil")
    @PreAuthorize("hasAuthority('VER_MI_PERFIL')")
    public ResponseEntity<?> verPerfilUsuario(){
        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        if (empleadoEmpresaService.existeEmpleadoPorUsuarioId(usuario.getId())) {
            return ResponseEntity.ok(empleadoEmpresaService.buscarEmpleadoEmpresaPorUsuarioId(usuario.getId()).get());
        } else if (empresaService.existeEmpresaPorUsuarioId(usuario.getId())) {
            return ResponseEntity.ok(empresaService.buscarEmpresaPorIdUsuario(usuario.getId()).get());
        } else if (candidatoService.existeCandidatoPorUsuarioId(usuario.getId())) {
            return ResponseEntity.ok(candidatoService.buscarCandidatoPorIdUsuario(usuario.getId()).get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tiene perfil asociado.");
        }
    }
}
