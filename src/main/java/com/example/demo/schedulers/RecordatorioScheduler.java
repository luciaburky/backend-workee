package com.example.demo.schedulers;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.entities.eventos.Evento;
import com.example.demo.entities.eventos.Notificacion;
import com.example.demo.entities.eventos.TipoNotificacion;
import com.example.demo.entities.seguridad.Usuario;
import com.example.demo.services.eventos.EventoService;
import com.example.demo.services.eventos.NotificacionService;

@Component
public class RecordatorioScheduler {
    
    private final EventoService eventoService;
    private final NotificacionService notificacionService;

    public RecordatorioScheduler(EventoService eventoService, NotificacionService notificacionService) {
        this.eventoService = eventoService;
        this.notificacionService = notificacionService;
    }

    @Scheduled(cron = "0 0/10 * * * *") // Ejecutar cada 10 min
    public void generarRecordatorios() {
        Date ahora = new Date();

        // Recordatorio 3 dias antes
        Date tresDiasDesde = new Date(ahora.getTime() + TimeUnit.DAYS.toMillis(3) - TimeUnit.HOURS.toMillis(1));
        Date tresDiasHasta = new Date(ahora.getTime() + TimeUnit.DAYS.toMillis(3) + TimeUnit.HOURS.toMillis(1));
        List<Evento> eventos3Dias = eventoService.obtenerEventosEntreFechas(tresDiasDesde, tresDiasHasta);

        for (Evento evento : eventos3Dias) {
            crearNotificacionRecordatorio(evento, "Recordatorio de evento (3 días)");
        }

        // Recordatorio 1 hora antes
        Date unaHoraDesde = new Date(ahora.getTime() + TimeUnit.HOURS.toMillis(1) - TimeUnit.MINUTES.toMillis(10));
        Date unaHoraHasta = new Date(ahora.getTime() + TimeUnit.HOURS.toMillis(1) + TimeUnit.MINUTES.toMillis(10));
        List<Evento> eventos1Hora = eventoService.obtenerEventosEntreFechas(unaHoraDesde, unaHoraHasta);

        for (Evento evento : eventos1Hora) {
            crearNotificacionRecordatorio(evento, "Recordatorio de evento (1 hora)");
        }

    }
    private void crearNotificacionRecordatorio(Evento evento, String titulo) {
        // Candidato
        if (evento.getUsuarioCandidato() != null) {
            String descripcion = "Tu evento " + evento.getNombreEvento() +
                    " está programado para " + evento.getFechaHoraInicioEvento();
            crearNotificacionParaUsuario(evento, evento.getUsuarioCandidato(), titulo, descripcion);
        }

        // Empleado
        if (evento.getUsuarioEmpleado() != null) {
            String descripcion = "Tu evento " + evento.getNombreEvento() +
                    " con el candidato está programado para " + evento.getFechaHoraInicioEvento();
            crearNotificacionParaUsuario(evento, evento.getUsuarioEmpleado(), titulo, descripcion); 
        }
    }

    private void crearNotificacionParaUsuario(Evento evento, Usuario usuario, String titulo, String descripcion) {
        Notificacion notificacion = new Notificacion();
        notificacion.setTituloNotificacion(titulo);
        notificacion.setDescripcionNotificacion(descripcion);
        notificacion.setLecturaNotificacion(false);
        notificacion.setTipoNotificacion(TipoNotificacion.RECORDATORIO_ENTREGA);
        notificacion.setUsuario(usuario);
        notificacion.setEvento(evento);
        notificacionService.crearNotificacion(notificacion);
    }


}
