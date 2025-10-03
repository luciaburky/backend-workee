package com.example.demo.schedulers;

import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.entities.eventos.Notificacion;
import com.example.demo.repositories.eventos.NotificacionRepository;

@Component
public class NotificacionScheduler {

    private final NotificacionRepository notificacionRepository;

    public NotificacionScheduler(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Scheduled(cron = "0 0 * * * *") // cada hora
    public void procesarNotificacionesPendientes() {
        Date ahora = new Date();

        List<Notificacion> pendientes = notificacionRepository.findByFechaHoraEnvioNotificacionBeforeAndEnviadaFalse(ahora);

        for (Notificacion notificacion : pendientes) {
            // TODO: Lógica para enviar al front
            System.out.println("Enviando notificación: " + notificacion.getDescripcionNotificacion());

            // Marcar como "enviada" 
            notificacion.setEnviada(true);
            notificacionRepository.save(notificacion);
        }
    }
}
