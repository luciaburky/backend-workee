package com.example.demo.services.postulaciones;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.postulaciones.PostulacionCandidatoRequestDTO;
import com.example.demo.entities.candidato.Candidato;
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
import com.example.demo.services.oferta.OfertaService;
import com.example.demo.services.params.EtapaService;

import jakarta.transaction.Transactional;

@Service
public class PostulacionOfertaServiceImpl extends BaseServiceImpl<PostulacionOferta, Long> implements PostulacionOfertaService{
    private final PostulacionOfertaRepository postulacionOfertaRepository;
    
    private final CandidatoService candidatoService;
    private final OfertaService ofertaService;
    private final EtapaService etapaService;

    public PostulacionOfertaServiceImpl(PostulacionOfertaRepository postulacionOfertaRepository, CandidatoService candidatoService, OfertaService ofertaService, EtapaService etapaService) {
        super(postulacionOfertaRepository);
        this.postulacionOfertaRepository = postulacionOfertaRepository;
        this.candidatoService = candidatoService;
        this.ofertaService = ofertaService;
        this.etapaService = etapaService;
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
        
        return postulacionOfertaRepository.save(postulacionOferta);
        //TODO: Faltaria que envie la solicitud de postulacion a la empresa (tiene que ver con las notificaciones)
        //TODO: verificar que no exista ya una postulacion del candidato a esta oferta
    }

    private Boolean verificarSiCandidatoYaPostulo(Long idCandidato, Long idOferta){
        Optional<PostulacionOferta> postulacionExistenteOptional = postulacionOfertaRepository.findByCandidatoIdAndOfertaIdAndFechaHoraFinPostulacionOfertaIsNull(idCandidato, idOferta);
        return postulacionExistenteOptional.isPresent();
    }
}

