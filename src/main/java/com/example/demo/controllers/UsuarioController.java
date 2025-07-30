package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.CorreoRequestDTO;
import com.example.demo.dtos.RecuperarContraseniaDTO;
import com.example.demo.dtos.UsuarioResponseDTO;
import com.example.demo.services.BajaOrquestadorService;
import com.example.demo.services.seguridad.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path ="/usuarios")
@Tag(name = "Usuario", description = "Controlador para operaciones de Usuario")

public class UsuarioController {
    private final UsuarioService usuarioService;
    private final BajaOrquestadorService bajaOrquestadorService;

    public UsuarioController(UsuarioService usuarioService, BajaOrquestadorService bajaOrquestadorService){
        this.usuarioService = usuarioService;
        this.bajaOrquestadorService = bajaOrquestadorService;
    }

    @Operation(summary = "Enviar correo para que usuario recupere su contraseña")
    @PutMapping("/recuperarContrasenia")
    public ResponseEntity<?> solicitarRecuperarContrasenia(@RequestBody CorreoRequestDTO correoRequestDTO){
        this.usuarioService.solicitarRecuperarContrasenia(correoRequestDTO.getCorreo());
        return ResponseEntity.status(HttpStatus.OK).body("Revise su correo para poder recuperar su contraseña");
    }

    @Operation(summary = "Usuario recupera su contraseña")
    @PutMapping("/recuperarContrasenia/{idUsuario}")
    public ResponseEntity<?> confirmarRecuperacionContrasenia(@PathVariable String idUsuario, @RequestBody RecuperarContraseniaDTO recuperarContraseniaDTO){
        this.usuarioService.confirmarRecuperacionContrasenia(idUsuario, recuperarContraseniaDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Contraseña recuperada exitosamente");
    }

    @Operation(summary = "Trae a todos los usuarios que se encuentran activos")
    @GetMapping("")
    public ResponseEntity<?> buscarUsuariosActivos(){
        List<UsuarioResponseDTO> usuarios = usuarioService.buscarUsuariosActivos();
        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }

    @Operation(summary = "Trae a todos los usuarios que se encuentran activos según el rol ingresado")
    @GetMapping("/{nombreRol}")
    public ResponseEntity<?> buscarUsuariosActivosPorRol(@PathVariable String nombreRol){
        List<UsuarioResponseDTO> usuarios = usuarioService.buscarUsuariosActivosPorRol(nombreRol);
        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }

    @Operation(summary = "Para dar de baja un usuario y su entidad relacionada")
    @PutMapping("/{idUsuario}")
    public ResponseEntity<?> darDeBajaUsuario(@PathVariable Long idUsuario){
        this.bajaOrquestadorService.darDeBajaUsuarioYEntidadRelacionada(idUsuario);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario dado de baja correctamente");
    }

}
