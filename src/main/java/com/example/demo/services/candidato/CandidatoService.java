package com.example.demo.services.candidato;

import java.util.List;

import com.example.demo.dtos.CandidatoRequestDTO;
import com.example.demo.entities.Candidato;
import com.example.demo.services.BaseService;

public interface CandidatoService extends BaseService<Candidato, Long> {
    
    Candidato crearCandidato(CandidatoRequestDTO candidatoDTO);
    
    Candidato modificarCandidato(Long idCandidato, CandidatoRequestDTO candidatoRequestDTO);
    
    List<Candidato> obtenerCandidatos();
}
