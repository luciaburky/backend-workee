package com.example.demo.services.postulaciones;

import java.util.List;

import com.example.demo.dtos.postulaciones.PostulacionCandidatoRequestDTO;
import com.example.demo.dtos.postulaciones.PostulacionSimplificadaDTO;
import com.example.demo.entities.postulaciones.PostulacionOferta;

public interface PostulacionOfertaService {
    public PostulacionSimplificadaDTO postularComoCandidato(PostulacionCandidatoRequestDTO postulacionCandidatoRequestDTO);

    public List<PostulacionOferta> obtenerPostulacionesDeUnCandidato(Long idCandidato);

    public PostulacionSimplificadaDTO abandonarPostulacionComoCandidato(Long idPostulacion);
}
