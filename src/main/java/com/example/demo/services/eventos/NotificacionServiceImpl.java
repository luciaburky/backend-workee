package com.example.demo.services.eventos;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entities.eventos.Evento;
import com.example.demo.entities.eventos.Notificacion;
import com.example.demo.entities.eventos.TemplateHelper;
import com.example.demo.entities.eventos.TipoNotificacion;
import com.example.demo.entities.seguridad.Usuario;
import com.example.demo.repositories.eventos.NotificacionRepository;
import com.example.demo.repositories.postulaciones.PostulacionOfertaRepository;
import com.example.demo.services.BaseServiceImpl;

import jakarta.transaction.Transactional;

@Service
public class NotificacionServiceImpl extends BaseServiceImpl<Notificacion, Long> implements NotificacionService{
    private final NotificacionRepository notificacionRepository;
    
    public NotificacionServiceImpl(NotificacionRepository notificacionRepository, PostulacionOfertaRepository postulacionOfertaRepository) {
        super(notificacionRepository);
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    @Transactional
    public Notificacion crearNotificacion(TipoNotificacion tipo, Map<String, Object> datos, Usuario usuarioDestino, Evento eventoRelacionado, Date fechaProgramada) {

        String descripcion = TemplateHelper.aplicarTemplate(tipo.getTemplate(), datos);

        Notificacion notificacion = new Notificacion();
        notificacion.setTituloNotificacion(tipo.getTitulo());
        notificacion.setDescripcionNotificacion(descripcion);
        notificacion.setFechaHoraEnvioNotificacion(fechaProgramada);
        notificacion.setLecturaNotificacion(false);
        notificacion.setTipoNotificacion(tipo);
        notificacion.setUsuario(usuarioDestino);

        // si la notificación tiene que ver con un evento
        if (eventoRelacionado != null) {
            notificacion.setEvento(eventoRelacionado);
        }

        return notificacionRepository.save(notificacion);
    }

    @Override
    public List<Notificacion> obtenerNotificacionesPorUsuario(Long idUsuario) {
        return notificacionRepository.findByUsuarioIdAndFechaHoraEnvioNotificacionBeforeOrderByFechaHoraEnvioNotificacionDesc(idUsuario, new Date());
    }

    @Override
    @Transactional
    public void marcarComoLeida(Long idNotificacion) {
        Notificacion notificacion = this.findById(idNotificacion);
        notificacion.setLecturaNotificacion(true);
        notificacionRepository.save(notificacion);
    }
}
