package com.example.demo.controllers.seguridad;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.RolRequestDTO;
import com.example.demo.entities.seguridad.Rol;
import com.example.demo.services.seguridad.RolService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path ="/roles")
@Tag(name = "Rol", description = "Controlador para operaciones de Rol")
public class RolController {
    private final RolService rolService;

    public RolController(RolService rolService){
        this.rolService = rolService;
    }

    @Operation(summary = "Obtiene todos los roles")
    @GetMapping("")    
    @PreAuthorize("hasAuthority('VER_ROLES')") 
    public ResponseEntity<?> obtenerRoles(){
        List<Rol> roles = rolService.obtenerRoles();
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

    @Operation(summary = "Crea un nuevo rol")
    @PostMapping("")
    @PreAuthorize("hasAuthority('CREAR_ROL')") 
    public ResponseEntity<?> crearRol(@RequestBody RolRequestDTO rolRequestDTO){
        Rol nuevoRol = rolService.crearRol(rolRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRol);
    }

    @Operation(summary = "Modificar un rol existente")
    @PutMapping("/{idRol}")
    @PreAuthorize("hasAuthority('MODIFICAR_ROL')") 
    public ResponseEntity<?> modificarRol(@RequestBody RolRequestDTO rolRequestDTO, @PathVariable Long idRol){
        Rol rolExistente = rolService.modificarRol(rolRequestDTO, idRol);
        return ResponseEntity.status(HttpStatus.OK).body(rolExistente);
    }

    @Operation(summary = "Deshabilitar un rol por su id")
    @DeleteMapping("/deshabilitar/{idRol}")
    @PreAuthorize("hasAuthority('HABILITACION_ROL')") 
    public ResponseEntity<?> deshabilitarRol(@PathVariable Long idRol){
        rolService.deshabilitarRol(idRol);
        return ResponseEntity.status(HttpStatus.OK).body("Rol deshabilitado correctamente");
    }

    @Operation(summary = "Habilitar un rol por su id")
    @PutMapping("/habilitar/{idRol}")
    @PreAuthorize("hasAuthority('HABILITACION_ROL')") 
    public ResponseEntity<?> habilitarRol(@PathVariable Long idRol){
        rolService.habilitarRol(idRol);
        return ResponseEntity.status(HttpStatus.OK).body("Rol habilitado correctamente");
    }

    @Operation(summary = "Traer roles activos segun una categoría")
    @GetMapping("/porCategoria/{idCategoria}")
    @PreAuthorize("hasAuthority('MODIFICAR_ROL_USUARIO')") 
    public ResponseEntity<?> buscarRolesSegunCategoria(@PathVariable Long idCategoria){
        List<Rol> roles = rolService.obtenerRolesSegunCategoria(idCategoria);
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }
}