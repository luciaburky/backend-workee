package com.example.demo.repositories.eventos;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.eventos.Notificacion;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface NotificacionRepository extends BaseRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioIdAndFechaHoraEnvioNotificacionBeforeOrderByFechaHoraEnvioNotificacionDesc(Long idUsuario,Date fechaActual);

    List<Notificacion> findByFechaHoraEnvioNotificacionBeforeAndEnviadaFalse(Date fecha);
}
