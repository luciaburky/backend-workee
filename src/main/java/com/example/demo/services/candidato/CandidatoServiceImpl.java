package com.example.demo.services.candidato;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.CandidatoRequestDTO;
import com.example.demo.entities.candidato.Candidato;
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

    public CandidatoServiceImpl(CandidatoRepository candidatoRepository, CandidatoMapper candidatoMapper, ProvinciaService provinciaService, GeneroService generoService, EstadoBusquedaService estadoBusquedaService, HabilidadService habilidadService) {
        super(candidatoRepository);
        this.candidatoRepository = candidatoRepository;
        this.candidatoMapper = candidatoMapper;
        this.provinciaService = provinciaService;
        this.generoService = generoService;
        this.estadoBusquedaService = estadoBusquedaService;
        this.habilidadService = habilidadService;
    }

    @Override
    @Transactional
    public Candidato crearCandidato(CandidatoRequestDTO candidatoDTO) {
        Candidato nuevoCandidato = candidatoMapper.toEntity(candidatoDTO);

        Provincia provincia = provinciaService.findById(candidatoDTO.getIdProvincia());
        Genero genero = generoService.findById(candidatoDTO.getIdGenero());
        //EstadoBusqueda estadoBusqueda = estadoBusquedaService.findById(candidatoDTO.getIdEstadoBusqueda());
        if(candidatoDTO.getIdEstadoBusqueda() != null){
            EstadoBusqueda estadoBusqueda = estadoBusquedaService.findById(candidatoDTO.getIdEstadoBusqueda());
            nuevoCandidato.setEstadoBusqueda(estadoBusqueda);
        }

        //Falta: CandidatoCV, CandidatoHabilidades
        nuevoCandidato.setProvincia(provincia);
        //nuevoCandidato.setEstadoBusqueda(estadoBusqueda);
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
        return candidatoRepository.save(candidatoOriginal);
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
