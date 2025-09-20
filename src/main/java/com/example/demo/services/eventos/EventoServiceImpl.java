package com.example.demo.services.eventos;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.eventos.EventoRequestDTO;
import com.example.demo.entities.eventos.Evento;
import com.example.demo.entities.params.TipoEvento;
import com.example.demo.entities.postulaciones.PostulacionOferta;
import com.example.demo.entities.postulaciones.PostulacionOfertaEtapa;
import com.example.demo.repositories.eventos.EventoRepository;
import com.example.demo.services.BaseServiceImpl;
import com.example.demo.services.params.TipoEventoService;
import com.example.demo.services.postulaciones.PostulacionOfertaService;

import jakarta.transaction.Transactional;

@Service
public class EventoServiceImpl extends BaseServiceImpl<Evento, Long> implements EventoService{

    private final EventoRepository eventoRepository;
    private final TipoEventoService tipoEventoService;
    //private final PostulacionOfertaEtapaService postulacionOfertaService;

    public EventoServiceImpl(EventoRepository eventoRepository, TipoEventoService tipoEventoService, PostulacionOfertaService postulacionOfertaService) {
        super(eventoRepository);
        this.eventoRepository = eventoRepository;
        this.tipoEventoService = tipoEventoService;
        //this.postulacionOfertaService = postulacionOfertaService;
    }

    @Override
    @Transactional
    public Evento crearEvento(EventoRequestDTO evento) {

        TipoEvento tipoEvento = tipoEventoService.findById(evento.getIdTipoEvento());
        //PostulacionOferta postulacionOferta = postulacionOfertaService.findById(evento.getIdPostulacionOfertaEtapa());

        Evento nuevoEvento = new Evento();
        nuevoEvento.setFechaHoraAlta(new Date());
        nuevoEvento.setTipoEvento(tipoEvento);
        //nuevoEvento.setPostulacionOfertaEtapa(postulacionOferta);
        nuevoEvento.setNombreEvento(evento.getNombreEvento());
        nuevoEvento.setDescripcionEvento(evento.getDescripcionEvento());
        nuevoEvento.setFechaHoraInicioEvento(evento.getFechaHoraInicioEvento());

        //Falta parte Videollamada
        return null;
    }

    @Override
    public Evento modificarEvento(Long id, EventoRequestDTO eventoRequestDTO) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
