package com.example.demo.services.candidato;

import java.util.List;


import com.example.demo.dtos.CandidatoRequestDTO;
import com.example.demo.entities.candidato.Candidato;
import com.example.demo.entities.params.Habilidad;
import com.example.demo.services.BaseService;

public interface CandidatoService extends BaseService<Candidato, Long> {
    
    Candidato crearCandidato(CandidatoRequestDTO candidatoDTO);
    
    Candidato modificarCandidato(Long idCandidato, CandidatoRequestDTO candidatoRequestDTO);
    
    List<Candidato> obtenerCandidatos();

    List<Habilidad> obtenerHabilidades(Long idCandidato);

    List<Habilidad> obtenerHabilidadesPorTipo(Long idCandidato, Long idTipoHabilidad);
    
    void actualizaroCrearCV(Long idCandidato, String cv);
    
    void eliminarCv(Long idCandidato);
}
