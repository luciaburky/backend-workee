package com.example.demo.services.eventos;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entities.eventos.Notificacion;
import com.example.demo.repositories.eventos.NotificacionRepository;
import com.example.demo.services.BaseServiceImpl;

import jakarta.transaction.Transactional;

@Service
public class NotificacionServiceImpl extends BaseServiceImpl<Notificacion, Long> implements NotificacionService{
    private final NotificacionRepository notificacionRepository;
    
    public NotificacionServiceImpl(NotificacionRepository notificacionRepository) {
        super(notificacionRepository);
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    public Notificacion crearNotificacion(Notificacion notificacion) {
        notificacion.setFechaHoraEnvioNotificacion(new Date());
        return notificacionRepository.save(notificacion);
    }

    @Override
    public List<Notificacion> obtenerNotificacionesPorUsuario(Long idUsuario) {
        return notificacionRepository.findByUsuarioIdOrderByFechaHoraEnvioNotificacionDesc(idUsuario);
    }

    @Override
    @Transactional
    public void marcarComoLeida(Long idNotificacion) {
        Notificacion notificacion = this.findById(idNotificacion);
        notificacion.setLecturaNotificacion(true);
        notificacionRepository.save(notificacion);
    }
}
