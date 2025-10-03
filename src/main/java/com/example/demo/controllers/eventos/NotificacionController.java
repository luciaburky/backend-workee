package com.example.demo.controllers.eventos;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.eventos.Notificacion;
import com.example.demo.services.eventos.NotificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/notificaciones")
@Tag(name = "Notificacion", description = "Controlador para operaciones de Notificacion")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @Operation(summary = "Obtener notificaciones por usuario")
    @GetMapping("/usuario/{id}")
    @PreAuthorize("hasAuthority('VER_NOTIFICACIONES')")
    public ResponseEntity<List<Notificacion>> obtenerNotificacionPorUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.obtenerNotificacionesPorUsuario(id));
    }
    
    @Operation(summary = "Marcar notificación como leída")
    @PutMapping("/{id}/leer")
    @PreAuthorize("hasAuthority('VER_NOTIFICACIONES')")
    public ResponseEntity<Void> marcarNotificacionComoLeida(@PathVariable Long id) {
        notificacionService.marcarComoLeida(id);
        return ResponseEntity.noContent().build();
    }

    
}
