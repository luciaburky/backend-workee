package com.example.demo.repositories.postulaciones;

import java.util.Optional;

import com.example.demo.entities.postulaciones.PostulacionOferta;
import com.example.demo.repositories.BaseRepository;

public interface PostulacionOfertaRepository extends BaseRepository<PostulacionOferta, Long> {

    public Optional<PostulacionOferta> findByCandidatoIdAndOfertaIdAndFechaHoraFinPostulacionOfertaIsNull(Long idCandidato, Long idOferta);
}
