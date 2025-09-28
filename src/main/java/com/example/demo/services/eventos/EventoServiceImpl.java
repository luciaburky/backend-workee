package com.example.demo.services.eventos;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.eventos.EventoRequestDTO;
import com.example.demo.entities.eventos.Evento;
import com.example.demo.entities.params.TipoEvento;
import com.example.demo.entities.postulaciones.PostulacionOfertaEtapa;
import com.example.demo.entities.seguridad.Usuario;
import com.example.demo.entities.videollamadas.Videollamada;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.repositories.eventos.EventoRepository;
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

    public EventoServiceImpl(EventoRepository eventoRepository, TipoEventoService tipoEventoService, PostulacionOfertaEtapaService postulacionOfertaEtapaService, UsuarioService usuarioService) {
        super(eventoRepository);
        this.eventoRepository = eventoRepository;
        this.tipoEventoService = tipoEventoService;
        this.postulacionOfertaEtapaService = postulacionOfertaEtapaService;
        this.usuarioService = usuarioService;
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

        //TODO: generar notificación y programar recordatorios
        return eventoGuardado;
    }

    @Override
    @Transactional
    public Evento modificarEvento(Long id, EventoRequestDTO eventoRequestDTO) {
        if(id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        Evento evento = findById(id);
    
        if(evento.getFechaHoraInicioEvento() != eventoRequestDTO.getFechaHoraInicioEvento() || (evento.getFechaHoraFinEvento() !=  eventoRequestDTO.getFechaHoraFinEvento())){
            evento.setFechaHoraInicioEvento(eventoRequestDTO.getFechaHoraInicioEvento());
            evento.setFechaHoraFinEvento(eventoRequestDTO.getFechaHoraFinEvento()); // opcional
            //TODO: Notificar cambios de horario a los usuarios involucrados
            //TODO: Reprogramar recordatorios
        }  
        if (evento.getDescripcionEvento() != eventoRequestDTO.getDescripcionEvento()) {
            evento.setDescripcionEvento(eventoRequestDTO.getDescripcionEvento());;
            //TODO: Notificar cambios de descripción a los usuarios involucrados
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
        eventoRepository.delete(evento);
        //TODO: Notificar a los usuarios involucrados
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

    
}
