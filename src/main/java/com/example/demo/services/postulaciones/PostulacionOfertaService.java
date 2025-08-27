package com.example.demo.services.postulaciones;

import java.util.List;

import com.example.demo.dtos.postulaciones.PostulacionCandidatoRequestDTO;
import com.example.demo.entities.postulaciones.PostulacionOferta;

public interface PostulacionOfertaService {
    public PostulacionOferta postularComoCandidato(PostulacionCandidatoRequestDTO postulacionCandidatoRequestDTO);

    public List<PostulacionOferta> obtenerPostulacionesDeUnCandidato(Long idCandidato);

    public PostulacionOferta abandonarPostulacionComoCandidato(Long idPostulacion);
}
