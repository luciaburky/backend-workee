package com.example.demo.services.candidato;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.CandidatoRequestDTO;
import com.example.demo.entities.Candidato;
import com.example.demo.entities.params.EstadoBusqueda;
import com.example.demo.entities.params.Genero;
import com.example.demo.entities.params.Provincia;
import com.example.demo.mappers.CandidatoMapper;
import com.example.demo.repositories.CandidatoRepository;
import com.example.demo.services.BaseServiceImpl;
import com.example.demo.services.params.EstadoBusquedaService;
import com.example.demo.services.params.GeneroService;
import com.example.demo.services.params.ProvinciaService;

import jakarta.transaction.Transactional;

@Service
public class CandidatoServiceImpl extends BaseServiceImpl<Candidato, Long> implements CandidatoService {

    private final CandidatoRepository candidatoRepository;
    private final CandidatoMapper candidatoMapper;
    private final ProvinciaService provinciaService;
    private final GeneroService generoService;
    private final EstadoBusquedaService estadoBusquedaService;

    public CandidatoServiceImpl(CandidatoRepository candidatoRepository, CandidatoMapper candidatoMapper, ProvinciaService provinciaService, GeneroService generoService, EstadoBusquedaService estadoBusquedaService) {
        super(candidatoRepository);
        this.candidatoRepository = candidatoRepository;
        this.candidatoMapper = candidatoMapper;
        this.provinciaService = provinciaService;
        this.generoService = generoService;
        this.estadoBusquedaService = estadoBusquedaService;
    }

    @Override
    @Transactional
    public Candidato crearCandidato(CandidatoRequestDTO candidatoDTO) {
        Candidato nuevoCandidato = candidatoMapper.toEntity(candidatoDTO);

        Provincia provincia = provinciaService.findById(candidatoDTO.getIdProvincia());
        Genero genero = generoService.findById(candidatoDTO.getIdGenero());
        EstadoBusqueda estadoBusqueda = estadoBusquedaService.findById(candidatoDTO.getIdEstadoBusqueda());

        //Falta: CandidatoCV, CandidatoHabilidades
        nuevoCandidato.setProvincia(provincia);
        nuevoCandidato.setEstadoBusqueda(estadoBusqueda);
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
}
