package com.example.demo.services.postulaciones;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.postulaciones.PostulacionCandidatoRequestDTO;
import com.example.demo.entities.candidato.Candidato;
import com.example.demo.entities.eventos.Notificacion;
import com.example.demo.entities.eventos.TipoNotificacion;
import com.example.demo.entities.oferta.CodigoEstadoOferta;
import com.example.demo.entities.oferta.Oferta;
import com.example.demo.entities.oferta.OfertaEstadoOferta;
import com.example.demo.entities.params.CodigoEtapa;
import com.example.demo.entities.params.Etapa;
import com.example.demo.entities.postulaciones.PostulacionOferta;
import com.example.demo.entities.postulaciones.PostulacionOfertaEtapa;
import com.example.demo.exceptions.EntityNotValidException;
import com.example.demo.repositories.postulaciones.PostulacionOfertaRepository;
import com.example.demo.services.BaseServiceImpl;
import com.example.demo.services.candidato.CandidatoService;
import com.example.demo.services.eventos.NotificacionService;
import com.example.demo.services.oferta.OfertaService;
import com.example.demo.services.params.EtapaService;

import jakarta.transaction.Transactional;

@Service
public class PostulacionOfertaServiceImpl extends BaseServiceImpl<PostulacionOferta, Long> implements PostulacionOfertaService{
    private final PostulacionOfertaRepository postulacionOfertaRepository;
    private final NotificacionService notificacionService;
    
    private final CandidatoService candidatoService;
    private final OfertaService ofertaService;
    private final EtapaService etapaService;

    public PostulacionOfertaServiceImpl(PostulacionOfertaRepository postulacionOfertaRepository, CandidatoService candidatoService, OfertaService ofertaService, EtapaService etapaService, NotificacionService notificacionService) {
        super(postulacionOfertaRepository);
        this.postulacionOfertaRepository = postulacionOfertaRepository;
        this.candidatoService = candidatoService;
        this.ofertaService = ofertaService;
        this.etapaService = etapaService;
        this.notificacionService = notificacionService;
    }

    @Override
    @Transactional
    public PostulacionOferta postularComoCandidato(PostulacionCandidatoRequestDTO postulacionCandidatoRequestDTO) {
        PostulacionOferta postulacionOferta = new PostulacionOferta();

        Boolean yaPostulo = this.verificarSiCandidatoYaPostulo(postulacionCandidatoRequestDTO.getIdCandidato(), postulacionCandidatoRequestDTO.getIdOferta());
        if(yaPostulo){
            throw new EntityNotValidException("El candidato ya se encuentra postulado a esta oferta");
        }

        //Seteo del candidato
        Candidato candidato = candidatoService.findById(postulacionCandidatoRequestDTO.getIdCandidato());
        postulacionOferta.setCandidato(candidato);

        //Seteo de la oferta
        Oferta oferta = ofertaService.findById(postulacionCandidatoRequestDTO.getIdOferta());
        
        //Ver que no se encuentre ni cerrada ni finalizada
        if(oferta.getFechaFinalizacion() != null){
            throw new EntityNotValidException("No es posible postular, porque la oferta ya se encuentra finalizada");
        }
        OfertaEstadoOferta ofertaEstadoOfertaActual = oferta.getEstadosOferta().stream()
            .filter(eo -> eo.getFechaHoraBaja() == null)
            .findFirst()
            .orElseThrow(() -> new EntityNotValidException("La oferta no tiene un estado actual asignado"));
        
        if(!ofertaEstadoOfertaActual.getEstadoOferta().getCodigo().equals(CodigoEstadoOferta.ABIERTA)){
            throw new EntityNotValidException("No es posible postular, porque la oferta no se encuentra abierta");
        }
        
        postulacionOferta.setOferta(oferta);

        //El candidato es el que inicia la postulacion
        postulacionOferta.setIdIniciadorPostulacion(postulacionCandidatoRequestDTO.getIdCandidato());

        postulacionOferta.setFechaHoraAlta(new Date());

        //Creacion de la primera etapa (PENDIENTE)
        if(postulacionOferta.getPostulacionOfertaEtapaList() == null){
            postulacionOferta.setPostulacionOfertaEtapaList(new ArrayList<>());
        }
        
        Etapa etapa = etapaService.obtenerEtapaPorCodigo(CodigoEtapa.PENDIENTE);

        PostulacionOfertaEtapa postulacionOfertaEtapa = new PostulacionOfertaEtapa();
        postulacionOfertaEtapa.setEtapa(etapa);
        postulacionOfertaEtapa.setFechaHoraAlta(new Date());
        
        postulacionOferta.getPostulacionOfertaEtapaList().add(postulacionOfertaEtapa);

        //Creacion de la notificacion para la empresa
        Notificacion notificacion = new Notificacion();
        notificacion.setFechaHoraAlta(new Date());
        notificacion.setTipoNotificacion(TipoNotificacion.SOLICITUD_POSTULACION_OFERTA);
        String descripcion = "El candidato " + candidato.getNombreCandidato() + " " + candidato.getApellidoCandidato() + " ha solicitado participar en la oferta " + oferta.getTitulo();
        notificacion.setDescripcionNotificacion(descripcion);
        String titulo = "Han solicitado participar en una oferta";
        notificacion.setTituloNotificacion(titulo);
        notificacion.setLecturaNotificacion(false);

        notificacionService.guardarNotificacion(notificacion);
        
        return postulacionOfertaRepository.save(postulacionOferta);
        //TODO: Faltaria que envie la solicitud de postulacion a la empresa
    }

    private Boolean verificarSiCandidatoYaPostulo(Long idCandidato, Long idOferta){
        Optional<PostulacionOferta> postulacionExistenteOptional = postulacionOfertaRepository.findByCandidatoIdAndOfertaIdAndFechaHoraFinPostulacionOfertaIsNull(idCandidato, idOferta);
        return postulacionExistenteOptional.isPresent();
    }

    @Override
    public List<PostulacionOferta> obtenerPostulacionesDeUnCandidato(Long idCandidato){
        return postulacionOfertaRepository.findByCandidatoId(idCandidato);
    }

    @Override
    public PostulacionOferta abandonarPostulacionComoCandidato(Long idPostulacion){
        PostulacionOferta postulacionOferta = this.findById(idPostulacion);

        //Verificar que el candidato no haya sido seleccionado
        PostulacionOfertaEtapa postulacionOfertaEtapaActual = postulacionOferta.getPostulacionOfertaEtapaList().stream()
            .filter(poe -> poe.getFechaHoraBaja() == null)
            .findFirst()
            .orElseThrow(() -> new EntityNotValidException("La postulacion no tiene una etapa actual asignada")
            );
        if(postulacionOfertaEtapaActual.getEtapa().getCodigoEtapa().equals(CodigoEtapa.SELECCIONADO)){
            throw new EntityNotValidException("No es posible abandonar la postulacion porque el candidato ya ha sido seleccionado");
        }
        
        //Verificar que la oferta no se encuentre finalizada
        OfertaEstadoOferta ofertaEstadoOferta = postulacionOferta.getOferta().getEstadosOferta().stream()
            .filter(eo -> eo.getFechaHoraBaja() == null)
            .findFirst()
            .orElseThrow(() -> new EntityNotValidException("La oferta no tiene un estado actual asignado"));

        if(ofertaEstadoOferta.getEstadoOferta().getCodigo().equals(CodigoEstadoOferta.FINALIZADA)){
            throw new EntityNotValidException("No es posible abandonar la postulacion porque la oferta se encuentra finalizada");
        }
        
        Etapa etapaAbandono = etapaService.obtenerEtapaPorCodigo(CodigoEtapa.ABANDONADO);
        postulacionOfertaEtapaActual.setFechaHoraBaja(new Date());

        PostulacionOfertaEtapa postulacionOfertaEtapaAbandono = new PostulacionOfertaEtapa();
        postulacionOfertaEtapaAbandono.setEtapa(etapaAbandono);
        postulacionOfertaEtapaAbandono.setFechaHoraAlta(new Date());

        postulacionOferta.getPostulacionOfertaEtapaList().add(postulacionOfertaEtapaAbandono);

        return postulacionOfertaRepository.save(postulacionOferta);
    }

}
