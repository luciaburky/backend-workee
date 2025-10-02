package com.example.demo.repositories.eventos;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.eventos.Notificacion;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface NotificacionRepository extends BaseRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioIdOrderByFechaHoraEnvioNotificacionDesc(Long idUsuario);
}
