package com.example.demo.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.demo.dtos.CandidatoRequestDTO;
import com.example.demo.entities.Candidato;

public interface CandidatoService extends BaseService<Candidato, Long> {
    
    Candidato crearCandidato(CandidatoRequestDTO candidatoDTO);
    
    Candidato modificarCandidato(Long idCandidato, CandidatoRequestDTO candidatoRequestDTO);
    
    //List<Candidato> buscarCandidatosActivos();

}
