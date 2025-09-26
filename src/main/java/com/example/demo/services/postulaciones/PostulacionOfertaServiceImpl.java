package com.example.demo.services.postulaciones;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.ofertas.CandidatoPostuladoDTO;
import com.example.demo.dtos.postulaciones.CambioPostulacionDTO;
import com.example.demo.dtos.postulaciones.EtapaActualPostulacionDTO;
import com.example.demo.dtos.postulaciones.PostulacionCandidatoRequestDTO;
import com.example.demo.dtos.postulaciones.PostulacionSimplificadaDTO;
import com.example.demo.dtos.postulaciones.RetroalimentacionDTO;
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

    public PostulacionOfertaServiceImpl(PostulacionOfertaRepository postulacionOfertaRepository, CandidatoService candidatoService, 
    OfertaService ofertaService, EtapaService etapaService, NotificacionService notificacionService) {
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
        PostulacionOferta postulacionOferta = creacionCosasGenericasPostulacion(postulacionCandidatoRequestDTO);
        
        //El candidato es el que inicia la postulacion
        postulacionOferta.setIdIniciadorPostulacion(postulacionCandidatoRequestDTO.getIdCandidato());

        //TODO: Faltaria que envie la solicitud de postulacion a la empresa

        postulacionOfertaRepository.save(postulacionOferta);
        
        PostulacionSimplificadaDTO postulacionSimplificada = crearPostulacionSimplificada(postulacionOferta);

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
    
        PostulacionSimplificadaDTO postulacionSimplificada = crearPostulacionSimplificada(postulacionOferta);
        

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

        //La retroalimentacion que se ingresa al momento de rechazar
        if(!cambioPostulacionDTO.getRetroalimentacion().isBlank()){
            ofertaEtapaActual.setRetroalimentacionEmpresa(cambioPostulacionDTO.getRetroalimentacion());
        }

        postulacion.getPostulacionOfertaEtapaList().add(nuevaPostulacionOfertaEtapa);

        postulacionOfertaRepository.save(postulacion);


        PostulacionSimplificadaDTO postulacionActualizada = crearPostulacionSimplificada(postulacion);

        //TODO: Falta agregar lo de las notificaciones
        return postulacionActualizada;

    }

    private Boolean transicionEsPermitida(Long idOferta, PostulacionOfertaEtapa ofertaEtapaActual, String codigoEtapaNueva){
        if(ofertaEtapaActual.getEtapa().getCodigoEtapa().equals(CodigoEtapa.ABANDONADO)){
            System.out.println("El candidato ha abandonado la oferta.");
            return false;
        }

        if(ofertaEtapaActual.getEtapa().getCodigoEtapa().equals(CodigoEtapa.NO_ACEPTADO)){
            System.out.println("El candidato ha rechazado la participación en la oferta.");
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

        PostulacionSimplificadaDTO postulacion = crearPostulacionSimplificada(postulacionOriginal);

        return postulacion;
    }

    @Override
    public List<CandidatoPostuladoDTO> traerCandidatosPostuladosAOferta(Long idOferta){
        return postulacionOfertaRepository.traerCandidatosPostulados(idOferta);
    }

    @Override
    public List<CandidatoPostuladoDTO> traerCandidatosPendientesPostuladosAOferta(Long idOferta){
        Oferta oferta = ofertaService.findById(idOferta);
        Long idEmpresa = oferta.getEmpresa().getId();

        return postulacionOfertaRepository.traerCandidatosPostuladosPendientes(idOferta, idEmpresa);
    }

    @Override
    @Transactional
    public Boolean aceptarSolicitudDePostulacionCandidato(Long idPostulacion){
        PostulacionOferta postulacion = this.findById(idPostulacion);


        List<PostulacionOfertaEtapa> postulacionOfertasEtapa = postulacion.getPostulacionOfertaEtapaList();

        PostulacionOfertaEtapa postulacionOfertaEtapaActual = postulacionOfertasEtapa.stream().
                            filter(oe -> oe.getFechaHoraBaja() == null)
                            .findFirst()
                            .orElseThrow(() -> new EntityNotValidException("La postulacion no tiene un estado actual asignado"));

        if(!postulacionOfertaEtapaActual.getEtapa().getCodigoEtapa().equals(CodigoEtapa.PENDIENTE)){
            throw new EntityNotValidException("No puede aceptar la postulacion del candidato porque no está 'Pendiente");
        }

        Oferta oferta = postulacion.getOferta();
        //Para obtener el nro de orden de la etapa Pendiente (igualmente deberia ser 1)
        OfertaEtapa ofertaEtapaDeOfertaActual = oferta.getOfertaEtapas().stream()
                                    .filter(oe -> oe.getEtapa().getCodigoEtapa().equals(CodigoEtapa.PENDIENTE))
                                    .findFirst()
                                    .orElseThrow(() -> new EntityNotValidException("No se encontró la etapa con el codigo buscado"));
        
        Integer nroEtapaPendiente = ofertaEtapaDeOfertaActual.getNumeroEtapa();

        //Obtener proxima etapa
        OfertaEtapa ofertaEtapaDeOfertaNueva = oferta.getOfertaEtapas().stream()
                                    .filter(oe -> oe.getNumeroEtapa() == nroEtapaPendiente + 1)
                                    .findFirst()
                                    .orElseThrow(() -> new EntityNotValidException("No se encontró la próxima etapa"));

        postulacionOfertaEtapaActual.setFechaHoraBaja(new Date());

        PostulacionOfertaEtapa postulacionOfertaEtapaNueva = new PostulacionOfertaEtapa();
        postulacionOfertaEtapaNueva.setEtapa(ofertaEtapaDeOfertaNueva.getEtapa());
        postulacionOfertaEtapaNueva.setFechaHoraAlta(new Date());
        
        postulacion.getPostulacionOfertaEtapaList().add(postulacionOfertaEtapaNueva);

        postulacionOfertaRepository.save(postulacion);

        //TODO: Falta lo de la notificacion
        return true;
    }

    @Override
    @Transactional
    public Boolean rechazarSolicitudDePostulacionDeCandidatoPendiente(Long idPostulacion, CambioPostulacionDTO cambioPostulacionDTO){
        if(cambioPostulacionDTO.getRetroalimentacion().isBlank() || cambioPostulacionDTO.getRetroalimentacion() == null){
            throw new EntityNotValidException("Si va a rechazar a un candidato, se debe dar retroalimentación");
        }
        PostulacionOferta postulacion = this.findById(idPostulacion);


        List<PostulacionOfertaEtapa> postulacionOfertasEtapa = postulacion.getPostulacionOfertaEtapaList();

        PostulacionOfertaEtapa postulacionOfertaEtapaActual = postulacionOfertasEtapa.stream().
                            filter(oe -> oe.getFechaHoraBaja() == null)
                            .findFirst()
                            .orElseThrow(() -> new EntityNotValidException("La postulacion no tiene un estado actual asignado"));

        if(!postulacionOfertaEtapaActual.getEtapa().getCodigoEtapa().equals(CodigoEtapa.PENDIENTE)){
            throw new EntityNotValidException("No puede aceptar la postulacion del candidato porque no está 'Pendiente");
        }
        
        
        postulacionOfertaEtapaActual.setFechaHoraBaja(new Date());
        postulacionOfertaEtapaActual.setRetroalimentacionEmpresa(cambioPostulacionDTO.getRetroalimentacion());
        
        Etapa etapaRechazado = etapaService.obtenerEtapaPorCodigo(CodigoEtapa.RECHAZADO);

        PostulacionOfertaEtapa postulacionOfertaEtapaNueva = new PostulacionOfertaEtapa();
        postulacionOfertaEtapaNueva.setEtapa(etapaRechazado);
        postulacionOfertaEtapaNueva.setFechaHoraAlta(new Date());
        
        postulacion.getPostulacionOfertaEtapaList().add(postulacionOfertaEtapaNueva);

        postulacionOfertaRepository.save(postulacion);

        //TODO: Falta lo de la notificacion
        return true;
    }


    @Override
    public List<PostulacionOferta> buscarPostulacionesCandidatosEnCurso(Long idOferta){
        return postulacionOfertaRepository.traerPostulacionesCandidatosEnCurso(idOferta);
    }


    @Override
    public List<CandidatoPostuladoDTO> traerCandidatosSeleccionados(Long idOferta){
        return postulacionOfertaRepository.traerCandidatosSeleccionados(idOferta);
    }

    @Override
    @Transactional
    public Boolean seleccionarCandidato(Long idPostulacion, Boolean soloEste){
        PostulacionOferta postulacionSeleccionada = findById(idPostulacion);

        PostulacionOfertaEtapa postulacionOfertaEtapaActual = postulacionSeleccionada.getPostulacionOfertaEtapaList().stream()
                                                                .filter(poe -> poe.getFechaHoraBaja() == null)
                                                                .findFirst()
                                                                .orElseThrow(() -> new EntityNotValidException("No se encontró la etapa actual"));
        
        // Finalizo la etapa actual de la postulacion
        postulacionOfertaEtapaActual.setFechaHoraBaja(new Date());
        //postulacionOfertaEtapaActual.setRetroalimentacionEmpresa("¡Felicidades! Has sido seleccionado");
        
        //Seteo de la etapa seleccionado de la postulacion
        Etapa etapaSeleccionado = etapaService.obtenerEtapaPorCodigo(CodigoEtapa.SELECCIONADO);
        PostulacionOfertaEtapa postulacionOfertaEtapaNueva = new PostulacionOfertaEtapa();
        postulacionOfertaEtapaNueva.setEtapa(etapaSeleccionado);
        postulacionOfertaEtapaNueva.setFechaHoraAlta(new Date());

        postulacionSeleccionada.getPostulacionOfertaEtapaList().add(postulacionOfertaEtapaNueva);
        postulacionSeleccionada.setFechaHoraFinPostulacionOferta(new Date());

        //Indicar que la oferta ha finalizado con éxito
        Oferta oferta = ofertaService.findById(postulacionSeleccionada.getOferta().getId());

        oferta.setFinalizadaConExito(true); 
        
        
        // En caso de que solo seleccione al candidato indicado, finalizar la oferta y rechazar a los que quedan
        if(soloEste){
            ofertaService.cambiarEstado(oferta.getId(), CodigoEstadoOferta.FINALIZADA);
            
            //Rechazar a todos excepto a la que se seleccionó
            List<PostulacionOferta> postulacionesARechazar = buscarPostulacionesCandidatosEnCurso(oferta.getId());
            postulacionesARechazar = postulacionesARechazar.stream().
                                                            filter(po -> po.getId() != idPostulacion)
                                                            .toList();
            String retroalimentacion = "Gracias por participar del proceso. En esta ocasión, otro candidato fue seleccionado, pero valoramos profundamente el tiempo y el esfuerzo que dedicaste. ¡Te deseamos mucho éxito en tu búsqueda!.";
            rechazarListado(postulacionesARechazar, retroalimentacion);
        }
        
        postulacionOfertaRepository.save(postulacionSeleccionada); 
        ofertaService.save(oferta);
        
        //TODO: Falta lo de la notificacion
        
        return true;
    }

    @Override
    @Transactional
    public Boolean rechazarListado(List<PostulacionOferta> postulaciones, String retroalimentacion){
        Etapa etapaRechazado = etapaService.obtenerEtapaPorCodigo(CodigoEtapa.RECHAZADO);
        
        for(PostulacionOferta postulacion : postulaciones){
            List<PostulacionOfertaEtapa> poeList = postulacion.getPostulacionOfertaEtapaList();
            PostulacionOfertaEtapa postulacionOfertaEtapaActual = poeList.stream().filter(poe -> poe.getFechaHoraBaja() == null)
                            .findFirst()
                            .orElseThrow(() -> new EntityNotValidException("La postulacion no tiene un estado actual asignado"));

            postulacionOfertaEtapaActual.setFechaHoraBaja(new Date());
            postulacionOfertaEtapaActual.setRetroalimentacionEmpresa(retroalimentacion);

            PostulacionOfertaEtapa postulacionOfertaEtapaNueva = new PostulacionOfertaEtapa();
            postulacionOfertaEtapaNueva.setFechaHoraAlta(new Date());
            postulacionOfertaEtapaNueva.setEtapa(etapaRechazado);

            postulacion.setFechaHoraFinPostulacionOferta(new Date());
            postulacion.getPostulacionOfertaEtapaList().add(postulacionOfertaEtapaNueva);

            postulacionOfertaRepository.save(postulacion);
            //TODO: Falta lo de la notificacion
        }

        return true;
    }

    @Override
    @Transactional
    public PostulacionSimplificadaDTO enviarRetroalimentacion(RetroalimentacionDTO retroalimentacionDTO){
        PostulacionOferta postulacion = findById(retroalimentacionDTO.getIdPostulacion());

        PostulacionOfertaEtapa postulacionOfertaEtapa = postulacion.getPostulacionOfertaEtapaList()
                                                            .stream()
                                                            .filter(poe -> poe.getId() == retroalimentacionDTO.getIdPostulacionOfertaEtapa())
                                                            .findFirst()
                                                            .orElseThrow(() -> new EntityNotValidException("No se encontró la postulacionOfertaEtapa buscada"));

        if(postulacionOfertaEtapa.getRetroalimentacionEmpresa() != null && !postulacionOfertaEtapa.getRetroalimentacionEmpresa().isBlank()){
            throw new EntityNotValidException("No es posible agregar retroalimentación ya que esta etapa ya posee.");
        } 
        postulacionOfertaEtapa.setRetroalimentacionEmpresa(retroalimentacionDTO.getRetroalimentacion());

        postulacionOfertaRepository.save(postulacion);

        PostulacionSimplificadaDTO postulacionSimplificadaDTO = crearPostulacionSimplificada(postulacion);

        return postulacionSimplificadaDTO;
    }
    
    private PostulacionSimplificadaDTO crearPostulacionSimplificada(PostulacionOferta postulacionOferta){
        PostulacionSimplificadaDTO postulacionSimplificada = new PostulacionSimplificadaDTO();

        postulacionSimplificada.setIdCandidato(postulacionOferta.getCandidato().getId());
        postulacionSimplificada.setIdIniciadorPostulacion(postulacionOferta.getIdIniciadorPostulacion());
        postulacionSimplificada.setIdOferta(postulacionOferta.getOferta().getId());
        postulacionSimplificada.setEtapas(postulacionOferta.getPostulacionOfertaEtapaList());
        postulacionSimplificada.setFechaHoraInicioPostulacion(postulacionOferta.getFechaHoraAlta());
        postulacionSimplificada.setFechaHoraAbandonoOferta(postulacionOferta.getFechaHoraAbandonoOferta());
        postulacionSimplificada.setFechaHoraFinPostulacionOferta(postulacionOferta.getFechaHoraFinPostulacionOferta());
        postulacionSimplificada.setIdPostulacionOferta(postulacionOferta.getId());

        return postulacionSimplificada;
    }


    @Override
    @Transactional
    public PostulacionSimplificadaDTO enviarRespuestaCandidato(RetroalimentacionDTO retroalimentacionDTO){
        PostulacionOferta postulacion = findById(retroalimentacionDTO.getIdPostulacion());
        
        PostulacionOfertaEtapa postulacionOfertaEtapa = postulacion.getPostulacionOfertaEtapaList()
                                                            .stream()
                                                            .filter(poe -> poe.getId() == retroalimentacionDTO.getIdPostulacionOfertaEtapa())
                                                            .findFirst()
                                                            .orElseThrow(() -> new EntityNotValidException("No se encontró la postulacionOfertaEtapa buscada"));


        Oferta oferta = ofertaService.findById(postulacion.getOferta().getId());

        OfertaEtapa ofertaEtapa = oferta.getOfertaEtapas()
                                        .stream()
                                        .filter(oe -> oe.getEtapa().getCodigoEtapa().equals(postulacionOfertaEtapa.getEtapa().getCodigoEtapa()))
                                        .findFirst()
                                        .orElseThrow(() -> new EntityNotValidException("No se encontró la postulacionOfertaEtapa buscada"));

        if(!ofertaEtapa.getAdjuntaEnlace()){
            throw new EntityNotValidException("No es posible agregar una respuesta ya que esta etapa no lo permite");
        }

        
        if(postulacionOfertaEtapa.getRespuestaCandidato() != null && !postulacionOfertaEtapa.getRespuestaCandidato().isBlank()){
            throw new EntityNotValidException("No es posible agregar una respuesta ya que esta etapa ya posee.");
        } 
        postulacionOfertaEtapa.setRespuestaCandidato(retroalimentacionDTO.getRetroalimentacion());

        postulacionOfertaRepository.save(postulacion);

        PostulacionSimplificadaDTO postulacionSimplificadaDTO = crearPostulacionSimplificada(postulacion);

        return postulacionSimplificadaDTO;
    }

    @Override
    @Transactional
    public PostulacionSimplificadaDTO enviarPostulacionACandidato(PostulacionCandidatoRequestDTO postulacionCandidatoRequestDTO) {
        Oferta oferta = ofertaService.findById(postulacionCandidatoRequestDTO.getIdOferta());

        PostulacionOferta postulacionOferta = creacionCosasGenericasPostulacion(postulacionCandidatoRequestDTO);
        //La empresa es quien inicia la postulacion
        postulacionOferta.setIdIniciadorPostulacion(oferta.getEmpresa().getId());

        
        
        //TODO: Faltaria que envie la solicitud de postulacion al candidato

        postulacionOfertaRepository.save(postulacionOferta);
        
        PostulacionSimplificadaDTO postulacionSimplificada = crearPostulacionSimplificada(postulacionOferta);

        return postulacionSimplificada;
    }

    @Transactional
    private PostulacionOferta creacionCosasGenericasPostulacion(PostulacionCandidatoRequestDTO postulacionCandidatoRequestDTO){
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

        return postulacionOferta;

    }

    @Override
    public EtapaActualPostulacionDTO verEtapaActualDeUnaPostulacion(Long idCandidato, Long idOferta){
        Optional<Etapa> etapaOptional = postulacionOfertaRepository.traerEtapaActualDePostulacionCandidato(idOferta, idCandidato);
        if(!etapaOptional.isPresent()){
            throw new EntityNotFoundException("No se encontró una etapa actual para el candidato");
        }
        Etapa etapa = etapaOptional.get();
        
        EtapaActualPostulacionDTO etapaActualPostulacionDTO = new EtapaActualPostulacionDTO();
        etapaActualPostulacionDTO.setCodigoEtapa(etapa.getCodigoEtapa());
        etapaActualPostulacionDTO.setNombreEtapa(etapa.getNombreEtapa());
        
        return etapaActualPostulacionDTO;
    }
    
}
