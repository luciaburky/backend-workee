package com.example.demo.services.oferta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.example.demo.dtos.OfertaRequestDTO;
import com.example.demo.entities.Base;
import com.example.demo.entities.empresa.Empresa;
import com.example.demo.entities.oferta.CodigoEstadoOferta;
import com.example.demo.entities.oferta.Oferta;
import com.example.demo.entities.oferta.OfertaEstadoOferta;
import com.example.demo.entities.oferta.OfertaEtapa;
import com.example.demo.entities.oferta.OfertaHabilidad;
import com.example.demo.entities.params.Habilidad;
import com.example.demo.entities.params.ModalidadOferta;
import com.example.demo.entities.params.TipoContratoOferta;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.mappers.OfertaMapper;
import com.example.demo.repositories.oferta.OfertaRepository;
import com.example.demo.services.BaseServiceImpl;
import com.example.demo.services.empresa.EmpresaService;
import com.example.demo.services.params.HabilidadService;
import com.example.demo.services.params.ModalidadOfertaService;
import com.example.demo.services.params.TipoContratoOfertaService;

@Service
public class OfertaServiceImpl extends BaseServiceImpl<Oferta, Long> implements OfertaService {

    private final OfertaRepository ofertaRepository;
    private final OfertaMapper ofertaMapper;
    private final EmpresaService empresaService;
    private final ModalidadOfertaService modalidadOfertaService;
    private final TipoContratoOfertaService tipoContratoOfertaService;
    private final OfertaEstadoOfertaService ofertaEstadoOfertaService;
    private final HabilidadService habilidadService;
    private final OfertaEtapaService ofertaEtapaService;

    public OfertaServiceImpl(OfertaRepository ofertaRepository, OfertaMapper ofertaMapper, EmpresaService empresaService, ModalidadOfertaService modalidadOfertaService, TipoContratoOfertaService tipoContratoOfertaService, OfertaEstadoOfertaService ofertaEstadoOfertaService, HabilidadService habilidadService, OfertaEtapaService ofertaEtapaService) {
        super(ofertaRepository);
        this.ofertaRepository = ofertaRepository;
        this.ofertaMapper = ofertaMapper;
        this.empresaService = empresaService;
        this.modalidadOfertaService = modalidadOfertaService;
        this.tipoContratoOfertaService = tipoContratoOfertaService;
        this.ofertaEstadoOfertaService = ofertaEstadoOfertaService;
        this.habilidadService = habilidadService; 
        this.ofertaEtapaService = ofertaEtapaService;
    }

    @Override
    @Transactional
    public Oferta crearOferta(OfertaRequestDTO ofertaDTO) {
        if(ofertaDTO == null) {
            throw new IllegalArgumentException("El objeto ofertaDTO no puede ser nulo");
        }
        
        //Campos simples
        Oferta oferta = ofertaMapper.toEntity(ofertaDTO);

        //Relaciones
        Empresa empresa = empresaService.findById(ofertaDTO.getIdEmpresa());
        ModalidadOferta modalidadOferta = modalidadOfertaService.findById(ofertaDTO.getIdModalidadOferta());
        TipoContratoOferta tipoContratoOferta = tipoContratoOfertaService.findById(ofertaDTO.getIdTipoContratoOferta());
        oferta.setEmpresa(empresa);
        oferta.setPais(empresa.getProvincia().getPais().getNombrePais());
        oferta.setModalidadOferta(modalidadOferta);
        oferta.setTipoContratoOferta(tipoContratoOferta);

        //Crear EstadoOferta Incial: Abierta
        OfertaEstadoOferta estadoInicial = ofertaEstadoOfertaService.abrirOferta();
        if (oferta.getEstadosOferta() == null) {
            oferta.setEstadosOferta(new ArrayList<>());
        }
        oferta.getEstadosOferta().add(estadoInicial);

        //Habilidades Etapa
        if (ofertaDTO.getIdHabilidades() != null && !ofertaDTO.getIdHabilidades().isEmpty()) {
            List<Habilidad> habilidadesDB = habilidadService.findAllById(ofertaDTO.getIdHabilidades());
            
            List<Long> idsDistintos = ofertaDTO.getIdHabilidades().stream()
                                                .distinct()
                                                .collect(Collectors.toList());

            Map<Long, Habilidad> porId = habilidadesDB.stream()
                .collect(Collectors.toMap(Habilidad::getId, Function.identity()));

            List<OfertaHabilidad> habilidades = new ArrayList<>();
            for (Long idHab : idsDistintos) {
                Habilidad hab = porId.get(idHab); 
                OfertaHabilidad oh = new OfertaHabilidad();
                oh.setFechaHoraAlta(new Date());
                oh.setHabilidad(hab);
                habilidades.add(oh);
            }
            
            if (oferta.getHabilidades() == null) {
                oferta.setHabilidades(new ArrayList<>());
            }
            oferta.getHabilidades().addAll(habilidades);
        }
        
        //OfertaEtapas
        if (ofertaDTO.getOfertaEtapas() != null && !ofertaDTO.getOfertaEtapas().isEmpty()) {
            List<OfertaEtapa> etapas = ofertaEtapaService.crearOfertaEtapasDesdeDto(
                empresa.getId(), 
                ofertaDTO.getOfertaEtapas()
            );
            if (oferta.getOfertaEtapas() == null) {
                oferta.setOfertaEtapas(new ArrayList<>());
            }
            oferta.getOfertaEtapas().addAll(etapas);
        }
    
        oferta.setFechaHoraAlta(new Date());
        oferta.setFinalizadaConExito(null); // Inicialmente no se sabe si la oferta se finalizó con éxito
        oferta.setFechaFinalizacion(null); // Inicialmente no hay fecha de finalización

        return ofertaRepository.save(oferta); // Implementar la lógica de creación de la oferta aquí
    }

    @Override
    @Transactional
    public Oferta cambiarEstado(Long ofertaId, String nuevoCodigo) {
        Oferta oferta = ofertaRepository.findById(ofertaId)
            .orElseThrow(() -> new EntityNotFoundException("Oferta no encontrada con ID: " + ofertaId));
        
        OfertaEstadoOferta estadoActual = oferta.getEstadosOferta()
            .stream()
            .filter(estado -> estado.getFechaHoraBaja() == null) // Filtrar estados activos
            .max(Comparator.comparing(Base::getFechaHoraAlta))
            .orElse(null);

    // 1) Si ya está FINALIZADA, no permito transiciones
        if (estadoActual != null 
                && CodigoEstadoOferta.FINALIZADA.equals(estadoActual.getEstadoOferta().getCodigo())) {
            throw new IllegalStateException("La oferta ya está FINALIZADA y no puede cambiar de estado.");
        }
    // 2) Si el estado actual es igual al nuevo, no hago nada
        if (estadoActual != null 
                && Objects.equals(estadoActual.getEstadoOferta().getCodigo(), nuevoCodigo)) {
            return oferta; 
        }

        OfertaEstadoOferta nuevoEstado;
        switch (nuevoCodigo) {
            case CodigoEstadoOferta.ABIERTA -> {
                nuevoEstado = ofertaEstadoOfertaService.abrirOferta();
            }
            case CodigoEstadoOferta.CERRADA -> {
                nuevoEstado = ofertaEstadoOfertaService.cerrarOferta();
            }
            case CodigoEstadoOferta.FINALIZADA -> {
                nuevoEstado = ofertaEstadoOfertaService.finalizarOferta();
                oferta.setFechaFinalizacion(new Date());
            }
            default -> throw new IllegalArgumentException("Código de estado inválido: " + nuevoCodigo);
        }

        if (estadoActual != null) {
            estadoActual.setFechaHoraBaja(new Date());
        }

        if (oferta.getEstadosOferta() == null) {
            oferta.setEstadosOferta(new ArrayList<>());
        }
        oferta.getEstadosOferta().add(nuevoEstado);

        return ofertaRepository.save(oferta);
    }

    @Override
    @Transactional
    public Oferta marcarResultadoFinal(Long ofertaId, boolean conExito) {
        Oferta oferta = ofertaRepository.findById(ofertaId)
            .orElseThrow(() -> new EntityNotFoundException("Oferta no encontrada con ID: " + ofertaId));

        // Debe estar FINALIZADA para poder marcar resultado
        OfertaEstadoOferta estadoActual = oferta.getEstadosOferta().stream()
            .filter(e -> e.getFechaHoraBaja() == null)
            .max(Comparator.comparing(Base::getFechaHoraAlta))
            .orElse(null);

        if (estadoActual == null 
            || !CodigoEstadoOferta.FINALIZADA.equals(estadoActual.getEstadoOferta().getCodigo())) {
            throw new IllegalStateException("Solo se puede marcar resultado cuando la oferta está FINALIZADA.");
        }

        oferta.setFinalizadaConExito(conExito);
        return ofertaRepository.save(oferta);
    }

    
}
