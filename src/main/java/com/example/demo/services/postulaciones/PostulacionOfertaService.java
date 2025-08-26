package com.example.demo.services.postulaciones;

import com.example.demo.dtos.postulaciones.PostulacionCandidatoRequestDTO;
import com.example.demo.entities.postulaciones.PostulacionOferta;

public interface PostulacionOfertaService {
    public PostulacionOferta postularComoCandidato(PostulacionCandidatoRequestDTO postulacionCandidatoRequestDTO);
}
