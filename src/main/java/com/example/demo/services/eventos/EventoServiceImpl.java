package com.example.demo.services.eventos;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.eventos.EventoRequestDTO;
import com.example.demo.entities.eventos.Evento;
import com.example.demo.entities.eventos.TipoNotificacion;
import com.example.demo.entities.params.TipoEvento;
import com.example.demo.entities.postulaciones.PostulacionOferta;
import com.example.demo.entities.postulaciones.PostulacionOfertaEtapa;
import com.example.demo.entities.seguridad.Usuario;
import com.example.demo.entities.videollamadas.Videollamada;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.repositories.eventos.EventoRepository;
import com.example.demo.repositories.postulaciones.PostulacionOfertaRepository;
import com.example.demo.services.BaseServiceImpl;
import com.example.demo.services.params.TipoEventoService;
import com.example.demo.services.postulaciones.PostulacionOfertaEtapaService;
import com.example.demo.services.seguridad.UsuarioService;

import jakarta.transaction.Transactional;

@Service
public class EventoServiceImpl extends BaseServiceImpl<Evento, Long> implements EventoService{


    private final EventoRepository eventoRepository;
    private final TipoEventoService tipoEventoService;
    private final PostulacionOfertaEtapaService postulacionOfertaEtapaService;
    private final UsuarioService usuarioService;
    private final NotificacionService notificacionService;
    private final PostulacionOfertaRepository postulacionOfertaRepository;

    public EventoServiceImpl(EventoRepository eventoRepository, TipoEventoService tipoEventoService, PostulacionOfertaEtapaService postulacionOfertaEtapaService, UsuarioService usuarioService, NotificacionService notificacionService, PostulacionOfertaRepository postulacionOfertaRepository) {
        super(eventoRepository);
        this.eventoRepository = eventoRepository;
        this.tipoEventoService = tipoEventoService;
        this.postulacionOfertaEtapaService = postulacionOfertaEtapaService;
        this.usuarioService = usuarioService;
        this.notificacionService = notificacionService;
        this.postulacionOfertaRepository = postulacionOfertaRepository;
    }

    @Override
    @Transactional
    public Evento crearEvento(EventoRequestDTO evento) {

        TipoEvento tipoEvento = tipoEventoService.findById(evento.getIdTipoEvento());
        PostulacionOfertaEtapa postulacionOfertaEtapa = postulacionOfertaEtapaService.findById(evento.getIdPostulacionOfertaEtapa());
        Usuario usuarioCandidato = usuarioService.findById(evento.getIdUsuarioCandidato());
        Usuario usuarioEmpleado = usuarioService.findById(evento.getIdUsuarioEmpleado());

        Evento nuevoEvento = new Evento();
        nuevoEvento.setNombreEvento(evento.getNombreEvento());
        nuevoEvento.setDescripcionEvento(evento.getDescripcionEvento());
        nuevoEvento.setFechaHoraInicioEvento(evento.getFechaHoraInicioEvento());
        nuevoEvento.setFechaHoraFinEvento(evento.getFechaHoraFinEvento()); // opcional
        nuevoEvento.setFechaHoraAlta(new Date());
        nuevoEvento.setTipoEvento(tipoEvento);
        nuevoEvento.setPostulacionOfertaEtapa(postulacionOfertaEtapa);
        
        // Setear usuarios relacionados
        nuevoEvento.setUsuarioCandidato(usuarioCandidato);
        nuevoEvento.setUsuarioEmpleado(usuarioEmpleado);
        
        /// Si es videollamada, instanciar la relación
        if ("Videollamada".equalsIgnoreCase(tipoEvento.getNombreTipoEvento())) {
            Videollamada videollamada = new Videollamada();
            videollamada.setEnlaceVideollamada(evento.getEnlaceVideollamada());
            videollamada.setFechaHoraInicioPlanifVideollamada(evento.getFechaHoraInicioEvento());
            videollamada.setFechaHoraFinPlanifVideollamada(evento.getFechaHoraFinEvento());

            // Inicializar campos vacíos
            videollamada.setFechaHoraInicioRealVideollamada(null);
            videollamada.setFechaHoraFinRealVideollamada(null);
            videollamada.setDuracionVideollamada(null);

            nuevoEvento.setVideollamada(videollamada);
        }

        Evento eventoGuardado = eventoRepository.save(nuevoEvento);

        // Crear Notificacion de Nuevo evento
        PostulacionOferta postulacionOferta = postulacionOfertaRepository
            .findByEtapaId(postulacionOfertaEtapa.getId())
            .orElseThrow(() -> new EntityNotFoundException("No se encontró la Postulación para el evento"));
        
        Map<String, Object> datosNotificacion = new HashMap<>();
        datosNotificacion.put("titulo", evento.getNombreEvento());  
        datosNotificacion.put("oferta", postulacionOferta.getOferta().getTitulo()); 
        datosNotificacion.put("empresa", postulacionOferta.getOferta().getEmpresa().getNombreEmpresa());
        datosNotificacion.put("candidato", postulacionOferta.getCandidato().getNombreCandidato());
        datosNotificacion.put("fecha", eventoGuardado.getFechaHoraInicioEvento().toString().split(" ")[0]);
        datosNotificacion.put("horas", eventoGuardado.getFechaHoraInicioEvento().toString().split(" ")[1]);

        // Notificación al candidato
        if ("Videollamada".equalsIgnoreCase(tipoEvento.getNombreTipoEvento())) {
            notificacionService.crearNotificacion(
                TipoNotificacion.EVENTO_VIDEOLLAMADA,
                datosNotificacion,
                usuarioCandidato,
                eventoGuardado,
                new Date() // enviar inmediatamente
            );
        } else {
            notificacionService.crearNotificacion(
                TipoNotificacion.EVENTO_ENTREGA,
                datosNotificacion,
                usuarioCandidato,
                eventoGuardado,
                new Date() 
            );
        }

        // Programar recordatorios (para candidato y empleado)
        Date fecha3DiasAntes = Date.from(eventoGuardado.getFechaHoraInicioEvento().toInstant().minus(3, ChronoUnit.DAYS));
        Date fecha1DiaAntes = Date.from(eventoGuardado.getFechaHoraInicioEvento().toInstant().minus(1, ChronoUnit.DAYS));
        
        if (usuarioCandidato != null) {
            notificacionService.crearNotificacion(
                TipoNotificacion.RECORDATORIO_EVENTO_3_DIAS_CANDIDATO,
                datosNotificacion,
                usuarioCandidato,
                eventoGuardado,
                fecha3DiasAntes
            );
            notificacionService.crearNotificacion(
                TipoNotificacion.RECORDATORIO_EVENTO_1_DIA_CANDIDATO,
                datosNotificacion,
                usuarioCandidato,
                eventoGuardado,
                fecha1DiaAntes
            );
        }
        if (usuarioEmpleado != null) {
            notificacionService.crearNotificacion(
                TipoNotificacion.RECORDATORIO_EVENTO_3_DIAS_EMPRESA,
                datosNotificacion,
                usuarioEmpleado,
                eventoGuardado,
                fecha3DiasAntes
            );
            notificacionService.crearNotificacion(
                TipoNotificacion.RECORDATORIO_EVENTO_1_DIA_EMPRESA,
                datosNotificacion,
                usuarioEmpleado,
                eventoGuardado,
                fecha1DiaAntes
            );
        }
        return eventoGuardado;
    }

    @Override
    @Transactional
    public Evento modificarEvento(Long id, EventoRequestDTO eventoRequestDTO) {
        if(id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        Evento evento = findById(id);
    
        if(!evento.getFechaHoraInicioEvento().equals(eventoRequestDTO.getFechaHoraInicioEvento())){
            evento.setFechaHoraInicioEvento(eventoRequestDTO.getFechaHoraInicioEvento());
            evento.setFechaHoraFinEvento(eventoRequestDTO.getFechaHoraFinEvento()); // opcional
            
            //TODO: Notificar cambios de horario a los usuarios involucrados
            PostulacionOferta postulacionOferta = postulacionOfertaRepository
                .findByEtapaId(evento.getPostulacionOfertaEtapa().getId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la Postulación para el evento"));

            Map<String, Object> datosNotificacion = new HashMap<>();
            datosNotificacion.put("titulo", evento.getNombreEvento());
            datosNotificacion.put("oferta", postulacionOferta.getOferta().getTitulo()); 
            datosNotificacion.put("empresa", postulacionOferta.getOferta().getEmpresa().getNombreEmpresa());
            datosNotificacion.put("fecha", eventoRequestDTO.getFechaHoraInicioEvento().toString().split(" ")[0]); // solo la fecha
            datosNotificacion.put("horas", eventoRequestDTO.getFechaHoraInicioEvento().toString().split(" ")[1]); // solo la hora

            if (evento.getUsuarioCandidato() != null) {
                notificacionService.crearNotificacion(
                    TipoNotificacion.EVENTO_MODIFICADO,
                    datosNotificacion, 
                    evento.getUsuarioCandidato(),     
                    evento,
                    new Date() 
                );        
            }

            //TODO: Reprogramar recordatorios
        }  
        if (!evento.getDescripcionEvento().equals(eventoRequestDTO.getDescripcionEvento())) {
            evento.setDescripcionEvento(eventoRequestDTO.getDescripcionEvento());;
        }

        return eventoRepository.save(evento);
    }

    @Override
    @Transactional
    public void eliminarEvento(Long idEvento) {
        if(idEvento == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        Evento evento = findById(idEvento);
        PostulacionOferta postulacionOferta = postulacionOfertaRepository
            .findByEtapaId(evento.getPostulacionOfertaEtapa().getId())
            .orElseThrow(() -> new EntityNotFoundException("No se encontró la Postulación para el evento"));

        Map<String, Object> datosNotificacion = new HashMap<>();
        datosNotificacion.put("titulo", evento.getNombreEvento());
        datosNotificacion.put("oferta", postulacionOferta.getOferta().getTitulo()); 
        datosNotificacion.put("empresa", postulacionOferta.getOferta().getEmpresa().getNombreEmpresa());

        if (evento.getUsuarioCandidato() != null) {
            notificacionService.crearNotificacion(
                TipoNotificacion.EVENTO_ELIMINADO,
                datosNotificacion, 
                evento.getUsuarioCandidato(),     
                evento, 
                new Date() 
            );        
        }

        eventoRepository.delete(evento); 
    }
    
    @Override
    @Transactional
    public List<Evento> obtenerEventosPorUsuario(Long idUsuario) {
        if(idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }

        List<Evento> listaEventos = eventoRepository.findByUsuarioCandidatoIdOrUsuarioEmpleadoId(idUsuario, idUsuario);

        if (listaEventos.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron Eventos para el Usuario con ID  " + idUsuario);
        }

        return listaEventos;
    }

    @Override
    @Transactional
    public List<Evento> obtenerEventosPorEmpresa(Long idEmpresa) {
        if(idEmpresa == null) {
            throw new IllegalArgumentException("El ID de la empresa no puede ser nulo");
        }

        List<Evento> listaEventos = eventoRepository.findEventosByEmpresaId(idEmpresa);

        if (listaEventos.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron Eventos para la Empresa con ID  " + idEmpresa);
        }

        return listaEventos;
    }

    @Override
    @Transactional
    public List<Evento> obtenerEventosEntreFechas(Date desde, Date hasta) {
        if(desde == null || hasta == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }
        if(desde.after(hasta)) {
            throw new IllegalArgumentException("La fecha 'desde' no puede ser posterior a la fecha 'hasta'");
        }

        List<Evento> listaEventos = eventoRepository.findEventosEntreFechas(desde, hasta);

        if (listaEventos.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron Eventos entre las fechas proporcionadas");
        }

        return listaEventos;
    }
}
