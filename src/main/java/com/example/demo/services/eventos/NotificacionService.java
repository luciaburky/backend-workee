package com.example.demo.services.eventos;

import java.util.List;

import com.example.demo.entities.eventos.Notificacion;

public interface NotificacionService {
    Notificacion crearNotificacion(Notificacion notificacion);
    
    List<Notificacion> obtenerNotificacionesPorUsuario(Long idUsuario);
    
    void marcarComoLeida(Long idNotificacion);
}