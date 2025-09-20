package com.example.demo.services.postulaciones;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.postulaciones.CambioPostulacionDTO;
import com.example.demo.dtos.postulaciones.EtapaActualPostulacionDTO;
import com.example.demo.dtos.postulaciones.PostulacionCandidatoRequestDTO;
import com.example.demo.dtos.postulaciones.PostulacionSimplificadaDTO;
import com.example.demo.entities.candidato.Candidato;
import com.example.demo.entities.oferta.CodigoEstadoOferta;
import com.example.demo.entities.oferta.Oferta;
import com.example.demo.entities.oferta.OfertaEstadoOferta;
import com.example.demo.entities.oferta.OfertaEtapa;
import com.example.demo.entities.params.CodigoEtapa;
import com.example.demo.entities.params.Etapa;
import com.example.demo.entities.postulaciones.PostulacionOferta;
import com.example.demo.entities.postulaciones.PostulacionOfertaEtapa;
import com.example.demo.exceptions.EntityNotFoundException;
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
    public PostulacionSimplificadaDTO postularComoCandidato(PostulacionCandidatoRequestDTO postulacionCandidatoRequestDTO) {
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
        /*Notificacion notificacion = new Notificacion();
        notificacion.setFechaHoraAlta(new Date());
        notificacion.setTipoNotificacion(TipoNotificacion.SOLICITUD_POSTULACION_OFERTA);
        String descripcion = "El candidato " + candidato.getNombreCandidato() + " " + candidato.getApellidoCandidato() + " ha solicitado participar en la oferta " + oferta.getTitulo();
        notificacion.setDescripcionNotificacion(descripcion);
        String titulo = "Han solicitado participar en una oferta";
        notificacion.setTituloNotificacion(titulo);
        notificacion.setLecturaNotificacion(false);

        notificacionService.guardarNotificacion(notificacion);*/
        
        //TODO: Faltaria que envie la solicitud de postulacion a la empresa

        postulacionOfertaRepository.save(postulacionOferta);
        
        PostulacionSimplificadaDTO postulacionSimplificada = new PostulacionSimplificadaDTO();
        postulacionSimplificada.setIdCandidato(postulacionOferta.getCandidato().getId());
        postulacionSimplificada.setIdIniciadorPostulacion(postulacionOferta.getIdIniciadorPostulacion());
        postulacionSimplificada.setIdOferta(postulacionOferta.getOferta().getId());
        postulacionSimplificada.setEtapas(postulacionOferta.getPostulacionOfertaEtapaList());
        postulacionSimplificada.setFechaHoraInicioPostulacion(postulacionOferta.getFechaHoraAlta());

        return postulacionSimplificada;
    }

    private Boolean verificarSiCandidatoYaPostulo(Long idCandidato, Long idOferta){
        Optional<PostulacionOferta> postulacionExistenteOptional = postulacionOfertaRepository.buscarPostulacionEnCurso(idCandidato, idOferta);
        return postulacionExistenteOptional.isPresent();
    }

    @Override
    public List<PostulacionSimplificadaDTO> obtenerPostulacionesDeUnCandidato(Long idCandidato){
        List<PostulacionOferta> postulaciones = postulacionOfertaRepository.findByCandidatoIdOrderByFechaHoraAltaDesc(idCandidato);
        return postulaciones.stream()
        .map(p -> new PostulacionSimplificadaDTO(
            p.getFechaHoraAlta(),
            p.getFechaHoraAbandonoOferta(),
            p.getFechaHoraFinPostulacionOferta(),
            p.getIdIniciadorPostulacion(),
            p.getCandidato().getId(),
            p.getId(),
            p.getOferta().getId(),
            p.getPostulacionOfertaEtapaList()
        ))
        .toList();
        //return postulacionOfertaRepository.findByCandidatoId(idCandidato);
    }

    @Override
    @Transactional
    public PostulacionSimplificadaDTO abandonarPostulacionComoCandidato(Long idPostulacion){
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

        if(postulacionOfertaEtapaActual.getEtapa().getCodigoEtapa().equals(CodigoEtapa.RECHAZADO)){
            throw new EntityNotValidException("No es posible abandonar la postulacion porque el candidato ya ha sido rechazado");
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
        postulacionOferta.setFechaHoraAbandonoOferta(new Date());

        postulacionOfertaRepository.save(postulacionOferta);
    
        PostulacionSimplificadaDTO postulacionSimplificada = new PostulacionSimplificadaDTO();
        postulacionSimplificada.setIdCandidato(postulacionOferta.getCandidato().getId());
        postulacionSimplificada.setIdIniciadorPostulacion(postulacionOferta.getIdIniciadorPostulacion());
        postulacionSimplificada.setIdOferta(postulacionOferta.getOferta().getId());
        postulacionSimplificada.setEtapas(postulacionOferta.getPostulacionOfertaEtapaList());
        postulacionSimplificada.setFechaHoraInicioPostulacion(postulacionOferta.getFechaHoraAlta());
        postulacionSimplificada.setFechaHoraAbandonoOferta(postulacionOferta.getFechaHoraAbandonoOferta());

        return postulacionSimplificada;
    }

    @Override
    public List<EtapaActualPostulacionDTO> buscarEtapasActualesDePostulacionesDeCandidato(Long idCandidato){
        return postulacionOfertaRepository.findEtapasActualesByCandidato(idCandidato);
    }

    @Override
    @Transactional
    public PostulacionSimplificadaDTO actualizarPostulacionDeCandidato(Long idPostulacion, CambioPostulacionDTO cambioPostulacionDTO){
        
        if(cambioPostulacionDTO.getCodigoEtapaNueva().equals(CodigoEtapa.SELECCIONADO)){
            throw new EntityNotValidException("Para cambiar a la etapa seleccionado debe usar otro endpoint");
        }
        if(cambioPostulacionDTO.getCodigoEtapaNueva().equals(CodigoEtapa.RECHAZADO)){
            if(cambioPostulacionDTO.getRetroalimentacion().isBlank()){
                throw new EntityNotValidException("Si va a rechazar a un candidato, se debe ingresar la retroalimentación.");
            }
        }
        
        Optional<PostulacionOferta> postulacionOptional = postulacionOfertaRepository.findById(idPostulacion);
        
        if(!postulacionOptional.isPresent()){
            throw new EntityNotFoundException("No se encontró la postulación buscada");
        }

        PostulacionOferta postulacion = postulacionOptional.get();

        List<PostulacionOfertaEtapa> ofertasEtapa = postulacion.getPostulacionOfertaEtapaList();

        PostulacionOfertaEtapa ofertaEtapaActual = ofertasEtapa.stream().
                            filter(oe -> oe.getFechaHoraBaja() == null)
                            .findFirst()
                            .orElseThrow(() -> new EntityNotValidException("La postulacion no tiene un estado actual asignado"));

        Boolean transicionPermitida = transicionEsPermitida(postulacion.getOferta().getId(), ofertaEtapaActual, cambioPostulacionDTO.getCodigoEtapaNueva());

        if(!transicionPermitida){
            throw new EntityNotValidException("No es posible actualizar la etapa. La transición seleccionada no está permitida");
        }
        
        ofertaEtapaActual.setFechaHoraBaja(new Date());

        //Seteo de la nueva etapa
        Etapa nuevaEtapa = etapaService.obtenerEtapaPorCodigo(cambioPostulacionDTO.getCodigoEtapaNueva());
        
        PostulacionOfertaEtapa nuevaPostulacionOfertaEtapa = new PostulacionOfertaEtapa();
        nuevaPostulacionOfertaEtapa.setEtapa(nuevaEtapa);
        nuevaPostulacionOfertaEtapa.setFechaHoraAlta(new Date());

        if(!cambioPostulacionDTO.getRetroalimentacion().isBlank()){
            ofertaEtapaActual.setRetroalimentacionEmpresa(cambioPostulacionDTO.getRetroalimentacion());
        }

        postulacion.getPostulacionOfertaEtapaList().add(nuevaPostulacionOfertaEtapa);

        postulacionOfertaRepository.save(postulacion);


        PostulacionSimplificadaDTO postulacionActualizada = new PostulacionSimplificadaDTO();
        postulacionActualizada.setEtapas(postulacion.getPostulacionOfertaEtapaList());
        postulacionActualizada.setFechaHoraInicioPostulacion(postulacion.getFechaHoraAlta());
        postulacionActualizada.setIdCandidato(postulacion.getCandidato().getId());
        postulacionActualizada.setIdIniciadorPostulacion(postulacion.getIdIniciadorPostulacion());
        postulacionActualizada.setIdOferta(postulacion.getOferta().getId());
        postulacionActualizada.setIdPostulacionOferta(idPostulacion);

        //TODO: Falta agregar lo de las notificaciones
        return postulacionActualizada;

    }

    private Boolean transicionEsPermitida(Long idOferta, PostulacionOfertaEtapa ofertaEtapaActual, String codigoEtapaNueva){
        if(ofertaEtapaActual.getEtapa().getCodigoEtapa().equals(CodigoEtapa.ABANDONADO)){
            System.out.println("El candidato ha abandonado la oferta.");
            return false;
        }

        if(ofertaEtapaActual.getEtapa().getCodigoEtapa().equals(CodigoEtapa.RECHAZADO)){
            System.out.println("El candidato ya ha sido rechazado.");
            
            return false;
        }

        if(ofertaEtapaActual.getEtapa().getCodigoEtapa().equals(CodigoEtapa.SELECCIONADO)){
            System.out.println("El candidato ya ha sido seleccionado.");
            return false;
        }
        if(ofertaEtapaActual.getEtapa().getCodigoEtapa().equals(codigoEtapaNueva)){
            System.out.println("No se puede actualizar a la misma etapa en la que estaba.");
            return false;
        }

        //Verificacion de orden en la oferta
        Oferta oferta = ofertaService.findById(idOferta);
        //Para obtener el nro de orden de la etapa actual
        OfertaEtapa ofertaEtapaDeOferta = oferta.getOfertaEtapas().stream()
                                    .filter(oe -> oe.getEtapa().getCodigoEtapa().equals(ofertaEtapaActual.getEtapa().getCodigoEtapa()))
                                    .findFirst()
                                    .orElseThrow(() -> new EntityNotValidException("No se encontró la etapa con el codigo buscado"));
        
        //Para obtener el nro de orden de la etapa nueva
        OfertaEtapa ofertaEtapaDeOfertaNueva = oferta.getOfertaEtapas().stream()
                                    .filter(oe -> oe.getEtapa().getCodigoEtapa().equals(codigoEtapaNueva))
                                    .findFirst()
                                    .orElseThrow(() -> new EntityNotValidException("No se encontró la etapa con el codigo buscado"));
        
        if(ofertaEtapaDeOferta.getNumeroEtapa() > ofertaEtapaDeOfertaNueva.getNumeroEtapa()){
            System.out.println("No es posible pasar a una etapa anterior.");
            return false;
        }
        return true;
    }

    @Override
    public PostulacionSimplificadaDTO verDetallePostulacionDeCandidato(Long idPostulacion){
        PostulacionOferta postulacionOriginal = findById(idPostulacion);

        PostulacionSimplificadaDTO postulacion = new PostulacionSimplificadaDTO();
        postulacion.setEtapas(postulacionOriginal.getPostulacionOfertaEtapaList());
        postulacion.setFechaHoraAbandonoOferta(postulacionOriginal.getFechaHoraAbandonoOferta());
        postulacion.setFechaHoraFinPostulacionOferta(postulacionOriginal.getFechaHoraFinPostulacionOferta());
        postulacion.setFechaHoraInicioPostulacion(postulacionOriginal.getFechaHoraAlta());
        postulacion.setIdCandidato(postulacionOriginal.getCandidato().getId());
        postulacion.setIdIniciadorPostulacion(postulacionOriginal.getIdIniciadorPostulacion());
        postulacion.setIdOferta(postulacionOriginal.getOferta().getId());
        postulacion.setIdPostulacionOferta(idPostulacion);

        return postulacion;
    }
}
