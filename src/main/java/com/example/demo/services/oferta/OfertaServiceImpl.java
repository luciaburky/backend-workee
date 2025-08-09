package com.example.demo.services.oferta;

import java.util.ArrayList;

import com.example.demo.dtos.OfertaRequestDTO;
import com.example.demo.entities.empresa.Empresa;
import com.example.demo.entities.oferta.Oferta;
import com.example.demo.entities.oferta.OfertaEstadoOferta;
import com.example.demo.entities.params.EstadoOferta;
import com.example.demo.entities.params.ModalidadOferta;
import com.example.demo.entities.params.TipoContratoOferta;
import com.example.demo.mappers.OfertaMapper;
import com.example.demo.repositories.oferta.OfertaRepository;
import com.example.demo.services.BaseServiceImpl;
import com.example.demo.services.empresa.EmpresaService;
import com.example.demo.services.params.EstadoOfertaService;
import com.example.demo.services.params.ModalidadOfertaService;
import com.example.demo.services.params.TipoContratoOfertaService;

import jakarta.transaction.Transactional;

public class OfertaServiceImpl extends BaseServiceImpl<Oferta, Long> implements OfertaService {

    private final OfertaRepository ofertaRepository;
    private final OfertaMapper ofertaMapper;
    private final EmpresaService empresaService;
    private final ModalidadOfertaService modalidadOfertaService;
    private final TipoContratoOfertaService tipoContratoOfertaService;
    private final OfertaEstadoOfertaService ofertaEstadoOfertaService;

    public OfertaServiceImpl(OfertaRepository ofertaRepository, OfertaMapper ofertaMapper, EmpresaService empresaService, ModalidadOfertaService modalidadOfertaService, TipoContratoOfertaService tipoContratoOfertaService, OfertaEstadoOfertaService ofertaEstadoOfertaService) {
        super(ofertaRepository);
        this.ofertaRepository = ofertaRepository;
        this.ofertaMapper = ofertaMapper;
        this.empresaService = empresaService;
        this.modalidadOfertaService = modalidadOfertaService;
        this.tipoContratoOfertaService = tipoContratoOfertaService;
        this.ofertaEstadoOfertaService = ofertaEstadoOfertaService;
    }

    @Override
    @Transactional
    public Oferta crearOferta(OfertaRequestDTO ofertaDTO) {
        if(ofertaDTO == null) {
            throw new IllegalArgumentException("El objeto ofertaDTO no puede ser nulo");
        }

        Oferta oferta = ofertaMapper.toEntity(ofertaDTO);

        //Buscar Relaciones
        Empresa empresa = empresaService.findById(ofertaDTO.getIdEmpresa());
        ModalidadOferta modalidadOferta = modalidadOfertaService.findById(ofertaDTO.getIdModalidadOferta());
        TipoContratoOferta tipoContratoOferta = tipoContratoOfertaService.findById(ofertaDTO.getIdTipoContratoOferta());
        oferta.setEmpresa(empresa);
        oferta.setPais(empresa.getProvincia().getPais().getNombrePais());
        oferta.setModalidadOferta(modalidadOferta);
        oferta.setTipoContratoOferta(tipoContratoOferta);

        //Crear primera instancia de EstadoOferta como Abierta
        OfertaEstadoOferta estadoInicial = ofertaEstadoOfertaService.abrirOferta();
        if (oferta.getEstadosOferta() == null) {
            oferta.setEstadosOferta(new ArrayList<>());
        }
        oferta.getEstadosOferta().add(estadoInicial);
        
        //TODO: OfertaHabilidades y OfertaEtapas
        if (ofertaDTO.getHabilidades() == null) {
            oferta.setHabilidades(new ArrayList<>());
        } else {
            return;
        }

        oferta.setFinalizadaConExito(null); // Inicialmente no se sabe si la oferta se finalizó con éxito
        oferta.setFechaFinalizacion(null); // Inicialmente no hay fecha de finalización

        return ofertaRepository.save(oferta); // Implementar la lógica de creación de la oferta aquí
    }
}
