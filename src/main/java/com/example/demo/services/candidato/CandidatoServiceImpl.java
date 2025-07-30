package com.example.demo.services.candidato;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.CandidatoRequestDTO;
import com.example.demo.entities.candidato.Candidato;
import com.example.demo.entities.candidato.CandidatoCV;
import com.example.demo.entities.candidato.CandidatoHabilidad;
import com.example.demo.entities.params.EstadoBusqueda;
import com.example.demo.entities.params.Genero;
import com.example.demo.entities.params.Habilidad;
import com.example.demo.entities.params.Provincia;
import com.example.demo.exceptions.EntityAlreadyExistsException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.mappers.CandidatoMapper;
import com.example.demo.repositories.candidato.CandidatoRepository;
import com.example.demo.services.BaseServiceImpl;
import com.example.demo.services.params.EstadoBusquedaService;
import com.example.demo.services.params.GeneroService;
import com.example.demo.services.params.HabilidadService;
import com.example.demo.services.params.ProvinciaService;

import jakarta.transaction.Transactional;

@Service
public class CandidatoServiceImpl extends BaseServiceImpl<Candidato, Long> implements CandidatoService {

    private final CandidatoRepository candidatoRepository;
    private final CandidatoMapper candidatoMapper;
    private final ProvinciaService provinciaService;
    private final GeneroService generoService;
    private final EstadoBusquedaService estadoBusquedaService;
    private final HabilidadService habilidadService;
    private final CandidatoCVService candidatoCVService;

    public CandidatoServiceImpl(CandidatoRepository candidatoRepository, CandidatoMapper candidatoMapper, ProvinciaService provinciaService, GeneroService generoService, EstadoBusquedaService estadoBusquedaService, HabilidadService habilidadService, CandidatoCVService candidatoCVService) {
        super(candidatoRepository);
        this.candidatoRepository = candidatoRepository;
        this.candidatoMapper = candidatoMapper;
        this.provinciaService = provinciaService;
        this.generoService = generoService;
        this.estadoBusquedaService = estadoBusquedaService;
        this.habilidadService = habilidadService;
        this.candidatoCVService = candidatoCVService;
    }

    @Override
    @Transactional
    public Candidato crearCandidato(CandidatoRequestDTO candidatoDTO) {
        Candidato nuevoCandidato = candidatoMapper.toEntity(candidatoDTO);

        Provincia provincia = provinciaService.findById(candidatoDTO.getIdProvincia());
        Genero genero = generoService.findById(candidatoDTO.getIdGenero());
        if(candidatoDTO.getIdEstadoBusqueda() != null){
            EstadoBusqueda estadoBusqueda = estadoBusquedaService.findById(candidatoDTO.getIdEstadoBusqueda());
            nuevoCandidato.setEstadoBusqueda(estadoBusqueda);
        }
        
        if(candidatoDTO.getIdHabilidades() != null && !candidatoDTO.getIdHabilidades().isEmpty()) {

            List<CandidatoHabilidad> habilidades = candidatoDTO.getIdHabilidades().stream()
            .distinct()
            .map(idHabilidad -> {
                Habilidad habilidad = habilidadService.findById(idHabilidad);
                CandidatoHabilidad candidatoHabilidad = new CandidatoHabilidad();
                candidatoHabilidad.setHabilidad(habilidad);
                candidatoHabilidad.setFechaHoraAlta(new Date());    
                return candidatoHabilidad;
            })
            .collect(Collectors.toList());

            nuevoCandidato.setHabilidades(habilidades);
        } else {
            nuevoCandidato.setHabilidades(new ArrayList<>());
        }

        //Falta: CandidatoCV
        if(candidatoDTO.getEnlaceCV() != null && !candidatoDTO.getEnlaceCV().isEmpty()) {
            CandidatoCV candidatoCV = candidatoCVService.actualizarOCrearCV(nuevoCandidato, candidatoDTO.getEnlaceCV());
            nuevoCandidato.setCandidatoCV(candidatoCV);
        }

        nuevoCandidato.setFechaHoraAlta(new Date());
        nuevoCandidato.setProvincia(provincia);
        nuevoCandidato.setGenero(genero);
        
        return candidatoRepository.save(nuevoCandidato);
    }
 
    @Override
    @Transactional
    public Candidato modificarCandidato(Long idCandidato, CandidatoRequestDTO candidatoDTO) {
        Candidato candidatoOriginal = findById(idCandidato);
        candidatoMapper.updateEntityFromDto(candidatoDTO, candidatoOriginal);
        
        if(candidatoDTO.getIdProvincia() != null && candidatoOriginal.getProvincia().getId() != candidatoDTO.getIdProvincia()) {
            Provincia nuevaProvincia = provinciaService.findById(candidatoDTO.getIdProvincia());
            candidatoOriginal.setProvincia(nuevaProvincia);
        }
        if(candidatoDTO.getIdEstadoBusqueda() != null && candidatoOriginal.getEstadoBusqueda().getId() != candidatoDTO.getIdEstadoBusqueda()) {
            EstadoBusqueda estadoBusqueda = estadoBusquedaService.findById(candidatoDTO.getIdEstadoBusqueda());
            candidatoOriginal.setEstadoBusqueda(estadoBusqueda);
        }
        if(candidatoDTO.getIdGenero() != null && candidatoOriginal.getGenero().getId() != candidatoDTO.getIdGenero()) {
            Genero genero = generoService.findById(candidatoDTO.getIdGenero());
            candidatoOriginal.setGenero(genero);
        }

        // Actualizar habilidades
        actualizarHabilidadesCandidato(candidatoOriginal, candidatoDTO);
        // Actualizar o crear CV
        candidatoCVService.actualizarOCrearCV(candidatoOriginal, candidatoDTO.getEnlaceCV());
        
        return candidatoRepository.save(candidatoOriginal);
    }

    @Override
    @Transactional
    public Candidato findById(Long id) {
        return candidatoRepository.findByIdWithHabilidades(id)
            .orElseThrow(() -> new EntityNotFoundException("Candidato no encontrado con ID: " + id));
    }
    
    @Override
    @Transactional
    public List<Candidato> obtenerCandidatos() {
        List<Candidato> listaCandidatos = candidatoRepository.findAllByOrderByNombreCandidatoAsc();
        return listaCandidatos;
    }

    @Override
    @Transactional
    public List<Habilidad> agregarHabilidad(Long idCandidato, Long idHabilidad) {
        Candidato candidato = candidatoRepository.findByIdWithHabilidades(idCandidato)
        .orElseThrow(() -> new EntityNotFoundException("Candidato no encontrado con ID " + idCandidato));

        Habilidad habilidad = habilidadService.findById(idHabilidad);
        
        // Verificar si la habilidad ya está asociada al candidato
        boolean yaExiste = candidato.getHabilidades().stream()
            .anyMatch(candidatoHabilidad -> candidatoHabilidad.getHabilidad().getId().equals(idHabilidad));

        // Agregar la nueva habilidad     
        if (!yaExiste) {
            CandidatoHabilidad nuevaHabilidad = new CandidatoHabilidad();
            nuevaHabilidad.setHabilidad(habilidad);
            nuevaHabilidad.setFechaHoraAlta(new Date());
            candidato.getHabilidades().add(nuevaHabilidad);
            candidatoRepository.save(candidato);
        } else {
            throw new EntityAlreadyExistsException("La habilidad ya está asociada al candidato.");
        }
        return obtenerHabilidades(idCandidato);
    }

    private void actualizarHabilidadesCandidato(Candidato candidato, CandidatoRequestDTO dto) {
        if (dto.getIdHabilidades() == null) return;

        Set<Long> nuevasIds = new HashSet<>(dto.getIdHabilidades());

        // Inicializar la lista si es null
        if (candidato.getHabilidades() == null) {
            candidato.setHabilidades(new ArrayList<>());
        }

        // Eliminar habilidades que no están en la nueva lista
        candidato.getHabilidades().removeIf(ch -> 
        !nuevasIds.contains(ch.getHabilidad().getId())
        );

        // Recupera los IDs actuales para comparar  
        Set<Long> idsActuales = candidato.getHabilidades()
            .stream()
            .map(ch -> ch.getHabilidad().getId())
            .collect(Collectors.toSet());

        // Agregar nuevas habilidades que no están en la lista actual
        for (Long idNueva : nuevasIds) {
            if (!idsActuales.contains(idNueva)) {
                Habilidad habilidad = habilidadService.findById(idNueva);
                CandidatoHabilidad candidatoHabilidad = new CandidatoHabilidad();
                candidatoHabilidad.setHabilidad(habilidad);
                candidatoHabilidad.setFechaHoraAlta(new Date());
                candidato.getHabilidades().add(candidatoHabilidad);
            }
        } 
    }


    @Override
    @Transactional
    public List<Habilidad> obtenerHabilidades(Long idCandidato) {
        Candidato candidato = candidatoRepository.findByIdWithHabilidades(idCandidato)
        .orElseThrow(() -> new EntityNotFoundException("Candidato no encontrado con ID " + idCandidato));        
        return candidato.getHabilidades()
                        .stream()
                        .map(CandidatoHabilidad::getHabilidad)
                        .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Habilidad> eliminarHabilidad(Long idCandidato, Long idHabilidad) {
        Candidato candidato = candidatoRepository.findByIdWithHabilidades(idCandidato)
        .orElseThrow(() -> new EntityNotFoundException("Candidato no encontrado con ID " + idCandidato));

        CandidatoHabilidad habilidadAEliminar = candidato.getHabilidades()
            .stream()
            .filter(ch -> ch.getHabilidad().getId().equals(idHabilidad))
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException("Habilidad no encontrada para el candidato con ID " + idCandidato));

        candidato.getHabilidades().remove(habilidadAEliminar);
        candidatoRepository.save(candidato);
        
        return obtenerHabilidades(idCandidato);
    }

    @Override
    @Transactional
    public List<Habilidad> obtenerHabilidadesPorTipo(Long idCandidato, Long idTipoHabilidad) {
        Candidato candidato = candidatoRepository.findByIdWithHabilidades(idCandidato)
        .orElseThrow(() -> new EntityNotFoundException("Candidato no encontrado con ID " + idCandidato));

        return candidato.getHabilidades()
                        .stream()
                        .map(CandidatoHabilidad::getHabilidad)
                        .filter(h -> h.getTipoHabilidad().getId().equals(idTipoHabilidad))
                        .collect(Collectors.toList());  
    }
}   
