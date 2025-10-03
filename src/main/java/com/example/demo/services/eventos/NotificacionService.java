package com.example.demo.services.eventos;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.demo.entities.eventos.Evento;
import com.example.demo.entities.eventos.Notificacion;
import com.example.demo.entities.eventos.TipoNotificacion;
import com.example.demo.entities.seguridad.Usuario;

public interface NotificacionService {
    
    Notificacion crearNotificacion(TipoNotificacion tipo, Map<String, Object> datos, Usuario usuarioDestino, Evento eventoRelacionado, Date fechaProgramada);    

    List<Notificacion> obtenerNotificacionesPorUsuario(Long idUsuario);
    
    void marcarComoLeida(Long idNotificacion);
}