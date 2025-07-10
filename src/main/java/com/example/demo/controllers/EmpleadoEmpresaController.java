package com.example.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.CrearEmpleadoEmpresaDTO;
import com.example.demo.dtos.ModificarEmpleadoEmpresaDTO;
import com.example.demo.entities.EmpleadoEmpresa;
import com.example.demo.services.EmpleadoEmpresaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path ="/empleados-empresa")
@Tag(name = "EmpleadoEmpresa", description = "Controlador para operaciones CRUD de EmpleadoEmpresa")
public class EmpleadoEmpresaController extends BaseControllerImpl<EmpleadoEmpresa, EmpleadoEmpresaService>{
    private final EmpleadoEmpresaService empleadoEmpresaService;

    public EmpleadoEmpresaController(EmpleadoEmpresaService empleadoEmpresaService) {
        super(empleadoEmpresaService);
        this.empleadoEmpresaService = empleadoEmpresaService;
    }

    @Operation(summary = "Crea un nuevo empleado de empresa")
    @PostMapping("/crearEmpleado")
    public ResponseEntity<EmpleadoEmpresa> crearEmpleadoEmpresa(@Valid @RequestBody CrearEmpleadoEmpresaDTO empleadoEmpresaRequestDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(empleadoEmpresaService.crearEmpleadoEmpresa(empleadoEmpresaRequestDTO));

            /*EmpleadoEmpresa nuevoEmpleado = empleadoEmpresaService.crearEmpleadoEmpresa(empleadoEmpresaRequestDTO);
            return ResponseEntity.ok(nuevoEmpleado);*/
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch(MethodArgumentNotValidException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Actualiza un empleado de una empresa")
    @PutMapping("/actualizarEmpleado/{id}")
    public ResponseEntity<EmpleadoEmpresa> actualizarEmpleadoEmpresa(@PathVariable Long id, @Valid @RequestBody ModificarEmpleadoEmpresaDTO modificarEmpleadoEmpresaDTO) {
        try {
            EmpleadoEmpresa empleadoActualizado = empleadoEmpresaService.actualizarEmpleado(modificarEmpleadoEmpresaDTO, id);
            return ResponseEntity.status(HttpStatus.OK).body(empleadoActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch(MethodArgumentNotValidException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}


