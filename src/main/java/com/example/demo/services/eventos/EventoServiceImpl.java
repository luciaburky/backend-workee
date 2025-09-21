package com.example.demo.services.eventos;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.eventos.EventoRequestDTO;
import com.example.demo.entities.eventos.Evento;
import com.example.demo.entities.params.TipoEvento;
import com.example.demo.entities.postulaciones.PostulacionOfertaEtapa;
import com.example.demo.entities.videollamadas.Videollamada;
import com.example.demo.repositories.eventos.EventoRepository;
import com.example.demo.services.BaseServiceImpl;
import com.example.demo.services.params.TipoEventoService;
import com.example.demo.services.postulaciones.PostulacionOfertaEtapaService;

import jakarta.transaction.Transactional;

@Service
public class EventoServiceImpl extends BaseServiceImpl<Evento, Long> implements EventoService{

    private final EventoRepository eventoRepository;
    private final TipoEventoService tipoEventoService;
    private final PostulacionOfertaEtapaService postulacionOfertaEtapaService;

    public EventoServiceImpl(EventoRepository eventoRepository, TipoEventoService tipoEventoService, PostulacionOfertaEtapaService postulacionOfertaEtapaService) {
        super(eventoRepository);
        this.eventoRepository = eventoRepository;
        this.tipoEventoService = tipoEventoService;
        this.postulacionOfertaEtapaService = postulacionOfertaEtapaService;
    }

    @Override
    @Transactional
    public Evento crearEvento(EventoRequestDTO evento) {

        TipoEvento tipoEvento = tipoEventoService.findById(evento.getIdTipoEvento());
        PostulacionOfertaEtapa postulacionOfertaEtapa = postulacionOfertaEtapaService.findById(evento.getIdPostulacionOfertaEtapa());

        Evento nuevoEvento = new Evento();
        nuevoEvento.setNombreEvento(evento.getNombreEvento());
        nuevoEvento.setDescripcionEvento(evento.getDescripcionEvento());
        nuevoEvento.setFechaHoraInicioEvento(evento.getFechaHoraInicioEvento());
        nuevoEvento.setFechaHoraFinEvento(evento.getFechaHoraFinEvento()); // opcional
        nuevoEvento.setFechaHoraAlta(new Date());
        nuevoEvento.setTipoEvento(tipoEvento);
        nuevoEvento.setPostulacionOfertaEtapa(postulacionOfertaEtapa);
        
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

        //TODO: generar notificación inicial y programar recordatorios
        return eventoGuardado;
    }

    @Override
    public Evento modificarEvento(Long id, EventoRequestDTO eventoRequestDTO) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
